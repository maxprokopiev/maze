(defproject maze "0.1.0-SNAPSHOT"
  :description "Solution for the Backend Developers Challenge"
  :url "http://github.com/juggler/maze"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main maze.core
  :dependencies [[org.clojure/clojure "1.5.0"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]]}})
