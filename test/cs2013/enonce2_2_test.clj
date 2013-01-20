(ns cs2013.enonce2-2-test
  (:use [midje.sweet]
        [cs2013.enonce2-2]))

(comment
  (def ^{:doc "input for tests"}
    in [{:VOL "MONAD42"  :DEPART 0 :DUREE 3 :PRIX 10}
        {:VOL "META18"   :DEPART 3 :DUREE 1 :PRIX 14}
        {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}])

  (def ^{:doc "Input for tests"}
    candidate
    '{:path [{:VOL "META18"}]
      :cmds ({:VOL "LEGACY01"} {:VOL "MONAD42"} {:VOL "YAGNI17"})}))

(fact "first level :path/:cmds"
  (in->candidates [{:VOL "META18"}
                   {:VOL "LEGACY01"}
                   {:VOL "MONAD42"}
                   {:VOL "YAGNI17"}]) => '({:path [{:VOL "META18"}], :cmds ({:VOL "LEGACY01"} {:VOL "MONAD42"} {:VOL "YAGNI17"})}
                                           {:path [{:VOL "LEGACY01"}], :cmds ({:VOL "META18"} {:VOL "MONAD42"} {:VOL "YAGNI17"})}
                                           {:path [{:VOL "MONAD42"}], :cmds ({:VOL "META18"} {:VOL "LEGACY01"} {:VOL "YAGNI17"})}
                                           {:path [{:VOL "YAGNI17"}], :cmds ({:VOL "META18"} {:VOL "LEGACY01"} {:VOL "MONAD42"})}
                                           {:path [{:VOL "META18"}], :cmds ()}
                                           {:path [{:VOL "LEGACY01"}], :cmds ()}
                                           {:path [{:VOL "MONAD42"}], :cmds ()}
                                           {:path [{:VOL "YAGNI17"}], :cmds ()}))

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
                               {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]}) => (contains
                                                                                   {:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                                           {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                                                                                    :cmds '({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 3}
                                                                                            {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5})}
                                                                                   {:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                                           {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5}]
                                                                                    :cmds '({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 3}
                                                                                            {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5})}
                                                                                   {:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                                           {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                                                                                    :cmds '()}
                                                                                   {:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                                           {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5}]
                                                                                    :cmds '()} :in-any-order))

(fact "only one children command is valid from this input"
  (candidate->commands '{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                         :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                 {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5})}) => (contains {:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                                                     {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}
                                                                                                     {:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}]
                                                                                              :cmds  ()}
                                                                                             {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                     {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                     {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                              :cmds '({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 5})}
                                                                                             :in-any-order))
(fact "both children commands are valids from this input"
  (candidate->commands '{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                         :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                 {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17})}) => (contains {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                      {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                      {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                               :cmds '({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 17})}
                                                                                              {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                      {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                      {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17}]
                                                                                               :cmds '({:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15})}
                                                                                              {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                      {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                      {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                               :cmds ()}
                                                                                              {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                      {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                      {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17}]
                                                                                               :cmds ()} :in-any-order))
(fact "Compute the children of one candidate"
  (candidates->candidates ['{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                    {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                             :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                     {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5})}
                           '{:path [{:DUREE 5 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                    {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}]
                             :cmds  ({:DUREE 7 :PRIX 14 :VOL "META18" :DEPART 15}
                                     {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17})}]) => (contains {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                           {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                           {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                                    :cmds '({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 5})}
                                                                                                   {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                           {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                           {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                                    :cmds '({:DUREE 9, :PRIX 7, :VOL "YAGNI17", :DEPART 17})}
                                                                                                   {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                           {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                           {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17}]
                                                                                                    :cmds '({:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15})}
                                                                                                   {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                           {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                           {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                                    :cmds '()}
                                                                                                   {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                           {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                           {:DUREE 7, :PRIX 14, :VOL "META18", :DEPART 15}]
                                                                                                    :cmds '()}
                                                                                                   {:path [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                                                                                                           {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}
                                                                                                           {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 17}]
                                                                                                    :cmds '()} :in-any-order))

(fact "compute the result from a possible command"
  (compute-result {:path
                   [{:DUREE 5, :PRIX 10, :VOL "MONAD42", :DEPART 0}
                    {:DUREE 9, :PRIX 8, :VOL "LEGACY01", :DEPART 5}]}) => {:gain 18 :path ["MONAD42" "LEGACY01"]})

(fact "Now real"
  (optimize [{:DUREE 5 :PRIX 1 :VOL "F514" :DEPART 0}]) => {:gain 1 :path ["F514"]}
  (optimize [{:VOL "AF1" :DEPART 0 :DUREE 1 :PRIX 2}
             {:VOL "AF2" :DEPART 4 :DUREE 1 :PRIX 4}
             {:VOL "AF3" :DEPART 2 :DUREE 1 :PRIX 6}]) => {:gain 12 :path ["AF1" "AF3" "AF2"]}
  (optimize [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
             {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
             {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
             {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => {:gain 18 :path ["MONAD42" "LEGACY01"]})

(fact
  (optimize [{:VOL "resonant-unit-62"            :DEPART 0   :DUREE 4 :PRIX 15}
             {:VOL "sparkling-ragweed-28"        :DEPART 1   :DUREE 2 :PRIX 1}
             {:VOL "nice-videodisc-57"           :DEPART 2   :DUREE 6 :PRIX 1}
             {:VOL "fancy-tambourine-7"          :DEPART 4   :DUREE 5 :PRIX 14}
             {:VOL "pleasant-sabotage-94"        :DEPART 5   :DUREE 2 :PRIX 18}
             {:VOL "thankful-veggie-90"          :DEPART 5   :DUREE 4 :PRIX 7}
             {:VOL "helpless-whaler-2"           :DEPART 6   :DUREE 2 :PRIX 2}
             {:VOL "crazy-collie-52"             :DEPART 7   :DUREE 6 :PRIX 3}
             {:VOL "obedient-bar-55"             :DEPART 9   :DUREE 5 :PRIX 15}
             {:VOL "frightened-strangeness-17"   :DEPART 10  :DUREE 2 :PRIX 4}
             {:VOL "squealing-moon-85"           :DEPART 10  :DUREE 4 :PRIX 8}
             {:VOL "squealing-tights-36"         :DEPART 11  :DUREE 2 :PRIX 7}
             {:VOL "melodic-bearer-87"           :DEPART 12  :DUREE 6 :PRIX 1}
             {:VOL "bad-dipstick-75"             :DEPART 14  :DUREE 5 :PRIX 11}
             {:VOL "gleaming-standby-50"         :DEPART 15  :DUREE 2 :PRIX 23}
             {:VOL "embarrassed-composer-15"     :DEPART 15  :DUREE 4 :PRIX 12}
             {:VOL "dark-transportation-18"      :DEPART 16  :DUREE 2 :PRIX 4}
             {:VOL "odd-smorgasbord-97"          :DEPART 17  :DUREE 6 :PRIX 2}
             {:VOL "tiny-wife-86"                :DEPART 19  :DUREE 5 :PRIX 20}
             {:VOL "gentle-slave-73"             :DEPART 20  :DUREE 2 :PRIX 3}
             {:VOL "careful-karaoke-87"          :DEPART 20  :DUREE 4 :PRIX 12}
             {:VOL "homeless-radiotherapy-20"    :DEPART 21  :DUREE 2 :PRIX 9}
             {:VOL "silly-sarcasm-56"            :DEPART 22  :DUREE 6 :PRIX 3}
             {:VOL "hollow-photography-8"        :DEPART 24  :DUREE 5 :PRIX 12}]) => {:gain 103
                                                                                      :path ["resonant-unit-62"
                                                                                             "pleasant-sabotage-94"
                                                                                             "obedient-bar-55"
                                                                                             "gleaming-standby-50"
                                                                                             "tiny-wife-86"
                                                                                             "hollow-photography-8"]})
