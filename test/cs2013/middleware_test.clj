(ns cs2013.middleware-test
  (:use [midje.sweet :only [fact]]
        [cs2013.middleware]))

(fact "Testing specific code story middleware"
  ;; nothing to do with non specific query
  ((wrap-correct-content-type identity) {})                                                  => {}
  ;; specific query dedicated to code story, they messed up their client
  ((wrap-correct-content-type identity) {:content-type "application/x-www-form-urlencoded"}) => {:content-type "application/json"})

(fact "Testing specific code story middleware"
  ((wrap-correct-encoding identity) {})                                => {:character-encoding "UTF-8"}
  ((wrap-correct-encoding identity) {:character-encoding "something"}) => {:character-encoding "something"})
