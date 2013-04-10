(ns maze.users-test
  (:use midje.sweet
        midje.util)
  (:require [maze.users :as users]))

(testable-privates maze.users process-message push)

(fact "about `process-message`"
  (expect (process-message "1\n" "1") => {"1" {:out "1" :followers #{}}}))

(fact "about `push`"
  (expect (push {} "1\n" "1") => {"1" {:out "1" :followers #{}}}))
