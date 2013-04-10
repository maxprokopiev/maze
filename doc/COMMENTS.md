# Comments

- task for this challenge I found here https://github.com/phoet/maze/blob/13_adapt_to_feedback/CHALLENGE.md (copied to doc/CHALLENGE.md)
- .jar files for test suite I got from this repo (https://github.com/nburkley/follower-maze) (copied to test_suite folder)

As there is the requirement "very minimal dependencies on third-party code" I only depend on Clojure 1.5.0 and built-in libraries. Except for leiningen - I think that leiningen is a de-facto standart for building/automating clojure projects, so I used it in development. However you don't need leiningen installed on your system, you can use maze-0.1.0-SNAPSHOT-standalone.jar instead.

Of course, there are possible drawbacks in this implementation. For example I'm using threads to handle connections - that's ok for 7 users, but it'll bad for thousands of them. In that case there should be some kind of an event-loop to process multiple connections.

## To run:

    lein run

or

    java -jar jar/maze-0.1.0-SNAPSHOT-standalone.jar

## To run tests:
   
    lein midje
