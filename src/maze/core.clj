(ns maze.core
  (:gen-class)
  (:require [maze.event-source :refer :all]
            [maze.users :refer :all]))

(defn- -main []
  (event-server 9090)
  (user-server 9099))
