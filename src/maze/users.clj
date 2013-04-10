(ns maze.users
  (:use [maze.sockets :only [create-server]])
  (:require [clojure.string :as string])
  (:import [java.io InputStreamReader BufferedReader OutputStreamWriter]))

(def users (agent {}))

(defn- process-message
  [message out]
  {(string/trim-newline message) {:out out :followers #{}}})

(defn- push
  [users message out]
  (merge users (process-message message out)))

(defn- user-processing
  [in out]
  (let [in (BufferedReader. (InputStreamReader. in))
        out (OutputStreamWriter. out "UTF-8")]
    (when-let [event (first (line-seq in))]
      (send-off users push event out)
      (flush))))

(defn user-server
  [port]
  (create-server port user-processing))
