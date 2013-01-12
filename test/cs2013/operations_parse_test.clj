(ns cs2013.operations-parse-test
  (:use [midje.sweet]
        [cs2013.operations-parse]))

(fact "inject"
  ((inject :c) :input) => [[:c :input]])

(fact "failure"
  ((failure) :input) => [])

(fact "item"
  ((item) "")          => []
  ((item) "abc") => [[\a '(\b \c)]]
  ((item) '(\a \b \c)) => [[\a '(\b \c)]])

(fact
  (parse (inject \a) "abc")   => [[\a "abc"]]
  (parse (failure) "abc")     => []
  (parse (item) "")           => []
  (parse (item) "abc")        => [[\a '(\b \c)]])
