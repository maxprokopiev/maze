(ns maze.sockets
  (:require [maze.threads :refer :all])
  (:import [java.net ServerSocket Socket SocketException]))

(defn- close-socket [^Socket s]
  (when-not (.isClosed s)
    (doto s
      (.shutdownInput)
      (.shutdownOutput)
      (.close))))

(defn- accept-fn [^Socket s fun]
  (let [ins (.getInputStream s)
        outs (.getOutputStream s)]
    (threadify #(do
                  (try
                    (fun ins outs)
                    (catch SocketException e))))))

(defn- create-server-internal [fun ^ServerSocket ss]
  (threadify #(when-not (.isClosed ss)
                (try
                  (accept-fn (.accept ss) fun)
                  (catch SocketException e))
                (recur))))

(defn create-server
  [port fun]
  (create-server-internal fun (ServerSocket. port)))
