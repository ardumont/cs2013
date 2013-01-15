(ns cs2013.enonce2-test
  (:use [midje.sweet]
        [cs2013.enonce2]))

(fact "Dummy fact"
      (optimize [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                 {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                 {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                 {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => {:gain 18 :path ["MONAD42" "LEGACY01"]})

(fact "sorting"
      (sort-by-duration [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                         {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                         {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                         {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => [{:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                                                                            {:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                                                                            {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                                                                            {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}])

(future-fact "not yet"
             (find-all-path-from {:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                                 [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                                  {:DEPART 3 :VOL "META18"   :DUREE 7 :PRIX 14}
                                  {:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
                                  {:DEPART 5 :VOL "YAGNI17"  :DUREE 9 :PRIX 7}]) => #{[0 5 9] [0 3 7]}

             (find-all-path-from {:DEPART 3 :VOL "META18"   :DUREE 7 :PRIX 14}
                                 [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                                  {:DEPART 3 :VOL "META18"   :DUREE 7 :PRIX 14}
                                  {:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
                                  {:DEPART 5 :VOL "YAGNI17"  :DUREE 9 :PRIX 7}]) => #{[3 7]}

             (find-all-path-from {:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
                                 [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                                  {:DEPART 3 :VOL "META18"   :DUREE 7 :PRIX 14}
                                  {:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
                                  {:DEPART 5 :VOL "YAGNI17"  :DUREE 9 :PRIX 7}]) => #{[5 9]})

(fact "Building a tree"
  (mktree {:dep 0 :arr 5 :prix 10}
          (mktree {:dep 5 :arr 14 :prix 7}
                  (mktree {:dep 14 :prix 7})
                  (mktree {:dep 14 :prix 8}))) =>
  '({:arr 5 :dep 0 :prix 10}
    ({:arr 14 :dep 5 :prix 7}
     ({:dep 14 :prix 7})
     ({:dep 14 :prix 8}))))

(fact "Build a tree from a starting point."
  (build-tree {:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
              [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
               {:DEPART 3 :VOL "META18"   :DUREE 7 :PRIX 14}
               {:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
               {:DEPART 5 :VOL "YAGNI17"  :DUREE 9 :PRIX 7}]) => '({:DEPART 0 :VOL "MONAD42" :DUREE 5 :PRIX 10} (({:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8})
                                                                                                                 ({:DEPART 5 :VOL "YAGNI17"  :DUREE 9 :PRIX 7}))))

(fact "Building all the possible trees from the data problems"
  (build-trees [{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                {:DEPART 3 :VOL "META18"   :DUREE 7 :PRIX 14}
                {:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}
                {:DEPART 5 :VOL "YAGNI17"  :DUREE 9 :PRIX 7}]) => [[{:DEPART 0 :VOL "MONAD42"  :DUREE 5 :PRIX 10}
                                                                    [[{:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}]
                                                                     [{:DEPART 5 :VOL "YAGNI17" :DUREE 9 :PRIX 7}]]]
                                                                   [{:DEPART 3 :VOL "META18"   :DUREE 7 :PRIX 14}]
                                                                   [{:DEPART 5 :VOL "LEGACY01" :DUREE 9 :PRIX 8}]
                                                                   [{:DEPART 5 :VOL "YAGNI17" :DUREE 9 :PRIX 7}]])
