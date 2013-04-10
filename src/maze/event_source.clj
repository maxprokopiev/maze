(ns maze.event-source
  (:use [maze.sockets :only [create-server]]
        [maze.users :only [users]])
  (:require [clojure.string :as string]
            [maze.events :refer :all])
  (:import [java.io InputStreamReader BufferedReader OutputStreamWriter]))

(def events (agent {}))
(def next-timestamp (atom 1))

(defn- process-event
  [message]
  (let [[timestamp command from to]
        (string/split (string/trim-newline message) #"\|")]
    {(Integer. timestamp)
      {:original message
       :command command
       :from from
       :to to}}))

(defn- timestamp
  [event]
  (:timestamp event))

(defn- send-event
  [event]
  (let [{command :command message :original from :from to :to} event]
    (case command
      "F" (send-off users follow message from to)
      "U" (send-off users unfollow message from to)
      "B" (send-off users broadcast message)
      "P" (send-off users private message to)
      "S" (send-off users status-update message from))))

(defn- process-events
  [events]
  (if (contains? events @next-timestamp)
    (do
      (send-event (get events @next-timestamp))
      (swap! next-timestamp inc)
      (recur (dissoc events (dec @next-timestamp))))
    events))

(defn- push
  [events event]
  (let [event (process-event event)
        events (conj events event)]
    (process-events events)))

(defn- event-processing
  [in out]
  (let [in (BufferedReader. (InputStreamReader. in))
        out (OutputStreamWriter. out "UTF-8")]
    (loop []
      (doseq [event (line-seq in)]
        (send-off events push event)
        (flush))
      (when-not (send-off events empty?)
        (recur)))))

(defn event-server
  [port]
  (create-server port event-processing))
