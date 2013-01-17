(ns cs2013.enonce2-test
  (:use [midje.sweet]
        [cs2013.enonce2]
        [clojure.tools.trace :only [trace] :as t]))

(fact "sorting"
  (sort-by-duration [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                     {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                     {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                     {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => [{:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                                                                        {:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                                                                        {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                                                                        {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}])

(fact "Building a tree"
  (mktree {:dep 0 :arr 5 :prix 10}
          (mktree {:dep 5 :arr 14 :prix 7}
                  (mktree {:dep 14 :prix 7})
                  (mktree {:dep 14 :prix 8})))
  => '({:arr 5 :dep 0 :prix 10}
       ({:arr 14 :dep 5 :prix 7}
        ({:dep 14 :prix 7})
        ({:dep 14 :prix 8}))))

(fact "Simple - Build a tree from a starting point."
  (build-tree {:DEPART 0  :DUREE 5}
              [{:DEPART 0 :DUREE 5}
               {:DEPART 3 :DUREE 3}
               {:DEPART 5 :DUREE 9}
               {:DEPART 6 :DUREE 10}
               {:DEPART 5 :DUREE 9}]) => '({:DEPART 0 :DUREE 5}
                                           ({:DEPART 5 :DUREE 9})
                                           ({:DEPART 6 :DUREE 10})
                                           ({:DEPART 5 :DUREE 9})))

(fact "Build a tree from a starting point."
  (build-tree {:DEPART 0  :DUREE 3}
              [{:DEPART 0 :DUREE 3}
               {:DEPART 3 :DUREE 3}
               {:DEPART 3 :DUREE 1}
               {:DEPART 5 :DUREE 9}
               {:DEPART 6 :DUREE 10}
               {:DEPART 5 :DUREE 9}]) => '({:DUREE 3 :DEPART 0}
                                           ({:DUREE 3 :DEPART 3}
                                            ({:DUREE 10 :DEPART 6}))
                                           ({:DUREE 1 :DEPART 3}
                                            ({:DUREE 9 :DEPART 5})
                                            ({:DUREE 10 :DEPART 6})
                                            ({:DUREE 9 :DEPART 5}))
                                           ({:DUREE 9 :DEPART 5})
                                           ({:DUREE 10 :DEPART 6})
                                           ({:DUREE 9 :DEPART 5})))

(fact "Building all the possible trees from the data problems"
             (build-trees [{:DEPART 0 :DUREE 5}
                           {:DEPART 3 :DUREE 7}
                           {:DEPART 5 :DUREE 9}
                           {:DEPART 5 :DUREE 9}]) => [[{:DEPART 0 :DUREE 5}
                                                       [{:DEPART 5 :DUREE 9}]
                                                       [{:DEPART 5 :DUREE 9}]]
                                                      [{:DEPART 3  :DUREE 7}]
                                                      [{:DEPART 5 :DUREE 9}]
                                                      [{:DEPART 5 :DUREE 9}]])

(fact "very basic"
  (find-all-path-from-tree nil) => [{:gain nil :path [nil]}])

(fact "less basic"
  (find-all-path-from-tree [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}]) => [{:gain 10 :path ["MONAD42"]}])

(fact "basic"
  (find-all-path-from-tree [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                            [{:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}]
                            [{:DEPART 5 :VOL "YAGNI17" :DUREE 9 :PRIX 7}]]) => [{:gain 18 :path ["MONAD42" "LEGACY01"]}
                                                                                {:gain 17 :path ["MONAD42" "YAGNI17"]}])
(fact "with depth"
  (find-all-path-from-tree [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                             [{:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
                              [{:DEPART 14 :VOL "META18" :DUREE 7 :PRIX 28}]]
                             [{:DEPART 5 :VOL "YAGNI17" :DUREE 9 :PRIX 7}]]) => [{:gain 46 :path ["MONAD42" "LEGACY01" "META18"]}
                                                                                 {:gain 17 :path ["MONAD42" "YAGNI17"]}])

(fact "with-more-depth"
  (find-all-path-from-tree [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                            [{:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
                             [{:DEPART 14 :VOL "META18" :DUREE 7 :PRIX 28}]]
                            [{:DEPART 5 :VOL "YAGNI17" :DUREE 9 :PRIX 7}
                             [{:DEPART 14 :VOL "ATM12" :DUREE 3 :PRIX 1}
                              [{:DEPART 17 :VOL "ATM121" :DUREE 4 :PRIX 20}]
                              [{:DEPART 17 :VOL "ATM122" :DUREE 3 :PRIX 21}]]]])
  => (contains {:gain 46 :path ["MONAD42" "LEGACY01" "META18"]}
               {:gain 39 :path ["MONAD42" "YAGNI17" "ATM12" "ATM122"]}
               {:gain 38 :path ["MONAD42" "YAGNI17" "ATM12" "ATM121"]} :in-any-order))

(fact "Given a list of paths, find the more interesting one (max of gain)"
  (best-paths [{:gain 46 :path ["MONAD42" "LEGACY01" "META18"]}
               {:gain 39 :path ["MONAD42" "YAGNI17" "ATM12" "ATM122"]}
               {:gain 38 :path ["MONAD42" "YAGNI17" "ATM12" "ATM121"]}]) => [{:gain 46 :path ["MONAD42" "LEGACY01" "META18"]}])

(fact "Now real"
      (optimize [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                 {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                 {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                 {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => {:gain 18 :path ["MONAD42" "LEGACY01"]})

(fact "Other test"
  (optimize '({:VOL "AF1" :DEPART 0 :DUREE 1 :PRIX 2}
              {:VOL "AF2" :DEPART 4 :DUREE 1 :PRIX 4}
              {:VOL "AF3" :DEPART 2 :DUREE 1 :PRIX 6})) => {:gain 12 :path ["AF1" "AF3" "AF2"]})
