(ns maze.events)

(defn- write
  [{out :out} message]
  (binding [*out* out]
    (println message)))

(defn follow
  [users message from to]
  (if-let [user (get users to)]
    (do
      (write user message)
      (update-in users [to :followers] conj from))
    users))

(defn private
  [users message to]
  (when-let [user (get users to)]
    (write user message))
  users)

(defn broadcast
  [users message]
  (doseq [user (vals users)]
    (write user message))
  users)

(defn unfollow
  [users message from to]
  (if (contains? users to)
    (update-in users [to :followers] disj from)
    users))

(defn status-update
  [users message from]
  (when-let [{followers :followers} (get users from)]
    (doseq [follower followers]
      (when-let [follower (get users follower)]
        (write follower message))))
  users)
