(ns maze.source
  (:use midje.sweet
        midje.util)
  (:require [maze.event-source :as event-source]))

(testable-privates maze.event-source process-event push)

(fact "about `process-event`"
  (let [event "1|F|2|3"]
    (expect (process-event event) => {1 {:original event
                                         :command "F"
                                         :from "2"
                                         :to "3"}})))

(fact "about `push`"
  (let [event "1|F|2|3"]
    (with-redefs-fn {#'event-source/process-events (fn [events] events)}
      #(expect (push {} event) => {1 {:original event
                                     :command "F"
                                     :from "2"
                                     :to "3"}}))))
