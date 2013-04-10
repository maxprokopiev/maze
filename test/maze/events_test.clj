(ns maze.events-test
  (:use midje.sweet)
  (:require [maze.events :as events]))

(def calls (atom []))

(defn with-call-recording
  [fun]
  (reset! calls [])
  (with-redefs-fn {#'events/write 
                   (fn [user message] (swap! calls conj [user message]))}
                  fun))

(def users
  {"23" {:out "23" :followers #{}}
   "12" {:out "12" :followers #{"23" "45"}}
   "45" {:out "45" :followers #{}}})

(defn get-followers
  [users user-id]
  (:followers (get users user-id)))

(defn get-receiviers
  [calls]
  (map #(:out (first %)) calls))

(fact "about `follow`"
  (with-call-recording
    #((expect 
         (get-followers (events/follow users "1|F|2|23" "2" "23") "23") => #{"2"})
    (expect (count @calls) => 1)
    (expect (get-receiviers @calls) => ["23"]))))

(fact "about `private`"
  (with-call-recording
    #((expect (events/private users "1|P|2|23" "23") => users)
      (expect (count @calls) => 1)
      (expect (get-receiviers @calls) => ["23"]))))

(fact "about `broadcast`"
  (with-call-recording
    #((expect (events/broadcast users "1|B") => users)
      (expect (count @calls) => 3)
      (expect (get-receiviers @calls) => ["23" "12" "45"]))))

(fact "about `unfollow`"
  (with-call-recording
    #((expect
        (get-followers (events/unfollow users "1|U|23|12" "23" "12") "12") => #{"45"})
      (expect (count @calls) => 0))))

(fact "about `status-update`"
  (with-call-recording
    #((expect (events/status-update users "1|S|12" "12") => users)
      (expect (count @calls) => 2)
      (expect (get-receiviers @calls) => ["23" "45"]))))
