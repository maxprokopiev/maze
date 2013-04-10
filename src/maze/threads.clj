(ns maze.threads)

(defn threadify
  [f]
  (doto (Thread. ^Runnable f)
    (.start)))
