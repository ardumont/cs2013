(ns cs2013.operations-parse-test
  (:use [midje.sweet]
        [cs2013.operations-parse]))

(fact "inject"
  ((inject :c) :input) => [[:c :input]])

(fact "failure"
  ((failure) :input) => [])

(fact "item"
  ((item) "abc") => [[\a '(\b \c)]]
  ((item) '(\a \b \c)) => [[\a '(\b \c)]])
