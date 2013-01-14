(ns cs2013.enonce1-test
  (:use [midje.sweet :only [fact]]
        [cs2013.enonce1]))

(fact "Trying out the first 10 numbers with decomposition"
  (map decomposition (range 0 10)) => '([{:foo 0  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 1  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 2  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 3  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 4  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 5  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 6  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 0  :bar 1  :qix 0  :baz 0}
                                           {:foo 7  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 1  :bar 1  :qix 0  :baz 0}
                                           {:foo 8  :bar 0  :qix 0  :baz 0}]
                                          [{:foo 2  :bar 1  :qix 0  :baz 0}
                                           {:foo 9  :bar 0  :qix 0  :baz 0}]))
