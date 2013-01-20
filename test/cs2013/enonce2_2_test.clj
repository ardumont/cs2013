(ns cs2013.enonce2-2-test
  (:use [midje.sweet]
        [cs2013.enonce2-2]))

(def ^{:doc "input for tests"}
  in [{:VOL "MONAD42"  :DEPART 0 :DUREE 3 :PRIX 10}
      {:VOL "META18"   :DEPART 3 :DUREE 1 :PRIX 14}
      {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}])

(def ^{:doc "Input for tests"}
  candidate
  '{:path [{:VOL "META18"}]
    :cmds ({:VOL "LEGACY01"} {:VOL "MONAD42"} {:VOL "YAGNI17"})})

(fact "first level :path/:cmds"
  (in->candidates [{:VOL "META18"}
                   {:VOL "LEGACY01"}
                   {:VOL "MONAD42"}
                   {:VOL "YAGNI17"}]) => '({:path [{:VOL "META18"}]
                                            :cmds ({:VOL "LEGACY01"} {:VOL "MONAD42"} {:VOL "YAGNI17"})}
                                           {:path [{:VOL "LEGACY01"}] :cmds ({:VOL "META18"} {:VOL "MONAD42"} {:VOL "YAGNI17"})}
                                           {:path [{:VOL "MONAD42"}]
                                            :cmds ({:VOL "META18"} {:VOL "LEGACY01"} {:VOL "YAGNI17"})}
                                           {:path [{:VOL "YAGNI17"}]
                                            :cmds ({:VOL "META18"} {:VOL "LEGACY01"} {:VOL "MONAD42"})}))



(fact "Is the following list of commands are invalid? (override path from one to the next)"
  (valid? [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
             {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
             {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
             {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => falsey)

(fact
  (valid? [{:VOL "META18"   :DEPART 3 :DUREE 1 :PRIX 14}
             {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
             {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => falsey)

(fact
  (valid? [{:VOL "META18"   :DEPART 3 :DUREE 1 :PRIX 14}
             {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => truthy)

(fact "Compute the valid commands of one candidate - None here are goods"
  (candidate->commands {:path [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}]
                        :cmds [{:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                               {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                               {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]}) => '())

(fact "Compute the children commands from this input, 2 are valids."
  (candidate->commands {:path [{:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}]
                        :cmds [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                               {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                               {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]}) => '({:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                                            {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                                                                                     :cmds ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 3}
                                                                                            {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5})}
                                                                                    {:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                                            {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5}]
                                                                                     :cmds ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 3}
                                                                                            {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5})}))

(fact "only one children command is valid from this input"
  (candidate->commands '{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                         :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                 {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5})}) => '({:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                             {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                             {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                      :cmds ({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 5})}))
(fact "both children commands are valids from this input"
   (candidate->commands '{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                 {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                          :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                  {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17})}) => '({:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                               {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                               {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                        :cmds ({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 17})}
                                                                                       {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                               {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                               {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17}]
                                                                                        :cmds ({:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15})}))
(fact "Compute the children of one candidate"
  (candidates->candidates ['{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                    {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                             :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                     {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5})}
                           '{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                    {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                             :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                     {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17})}]) => '({:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                   {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                   {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                            :cmds ({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 5})}
                                                                                           {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                   {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                   {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                            :cmds ({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 17})}
                                                                                           {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                   {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                   {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17}]
                                                                                            :cmds ({:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15})}))
