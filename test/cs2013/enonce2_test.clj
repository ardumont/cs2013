(ns cs2013.enonce2-test
  (:use [midje.sweet]
        [cs2013.enonce2]
        [clojure.tools.trace :only [trace] :as t]))

(fact "Compute all the nodes and group them by identity"
  (nodes-and-adjacents [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                        {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                        {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                        {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => [{"META18" {:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                                                                            "LEGACY01" {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                                                                            "MONAD42" {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                                                                            "YAGNI17" {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}}
                                                                           {"META18" ()
                                                                            "LEGACY01" ()
                                                                            "MONAD42" ["LEGACY01" "YAGNI17"]
                                                                            "YAGNI17" ()}])

(fact
  (nodes-and-adjacents [{:VOL "META18"   :DEPART 3 :DUREE 1 :PRIX 14}
                        {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                        {:VOL "LEGACY11" :DEPART 5 :DUREE 9 :PRIX 13}
                        {:VOL "MONAD42"  :DEPART 0 :DUREE 1 :PRIX 10}
                        {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => [{"YAGNI17" {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5}
                                                                            "MONAD42" {:DUREE 1 :PRIX 10 :VOL "MONAD42" :DEPART 0}
                                                                            "LEGACY11" {:DUREE 9 :PRIX 13 :VOL "LEGACY11" :DEPART 5}
                                                                            "LEGACY01" {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}
                                                                            "META18" {:DUREE 1 :PRIX 14 :VOL "META18" :DEPART 3}}
                                                                           {"YAGNI17" ()
                                                                            "MONAD42" '("META18" "LEGACY01" "LEGACY11" "YAGNI17")
                                                                            "LEGACY11" ()
                                                                            "LEGACY01" ()
                                                                            "META18" '("LEGACY01" "LEGACY11" "YAGNI17")}])

(fact "Simple - Build a tree from a starting point."
    (build-tree {"YAGNI17" {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5}
                 "MONAD42" {:VOL "MONAD42"  :DEPART 0 :DUREE 1 :PRIX 10}
                 "LEGACY11" {:DUREE 9 :PRIX 13 :VOL "LEGACY11" :DEPART 5}
                 "LEGACY01" {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}
                 "META18"  {:DUREE 1 :PRIX 14 :VOL "META18" :DEPART 3}}
                {"META18" ()
                 "LEGACY01" ()
                 "MONAD42" ["LEGACY01"
                            "YAGNI17"]
                 "YAGNI17" ["META18"]}
                {:id "MONAD42"
                 :gain 10
                 :path ["MONAD42"]})
    => '({:gain 10 :path ["MONAD42"] :id "MONAD42"}
         {:id "LEGACY01" :gain 18 :path ["MONAD42" "LEGACY01"]}
         {:id "YAGNI17" :gain 17 :path ["MONAD42" "YAGNI17"]}
         {:id "META18" :gain 31 :path ["MONAD42" "YAGNI17" "META18"]}))

(fact
  (build-tree {"YAGNI17"  {:DUREE 9 :PRIX 7 :VOL "YAGNI17" :DEPART 5}
               "MONAD42"  {:VOL "MONAD42"  :DEPART 0 :DUREE 1 :PRIX 10}
               "LEGACY11" {:DUREE 9 :PRIX 13 :VOL "LEGACY11" :DEPART 5}
               "LEGACY01" {:DUREE 9 :PRIX 8 :VOL "LEGACY01" :DEPART 5}
               "META18"   {:DUREE 1 :PRIX 14 :VOL "META18" :DEPART 3}
               "CYAGLE1"  {:DUREE 1 :PRIX 12 :VOL "CYAGLE1" :DEPART 2}}
              {"YAGNI17" ()
               "MONAD42" '("META18"
                           "LEGACY01"
                           "LEGACY11"
                           "YAGNI17")
               "LEGACY11" '("CYAGLE1")
               "LEGACY01" ()
               "META18" '("LEGACY01"
                          "LEGACY11"
                          "YAGNI17")
               "CYAGLE1" ()}
              {:id "META18"
               :gain 14
               :path ["META18"]}) => '({:gain 14 :path ["META18"] :id "META18"}
              {:id "LEGACY01" :gain 22 :path ["META18" "LEGACY01"]}
              {:id "LEGACY11" :gain 27 :path ["META18" "LEGACY11"]}
              {:id "YAGNI17" :gain 21 :path ["META18" "YAGNI17"]}
              {:id "CYAGLE1" :gain 39 :path ["META18" "LEGACY11" "CYAGLE1"]}))

(fact "Given a list of paths find the more interesting one (max of gain)"
  (best-path [{:gain 46 :path ["MONAD42" "LEGACY01" "META18"]}
              {:gain 39 :path ["MONAD42" "YAGNI17" "ATM12" "ATM122"]}
              {:gain 38 :path ["MONAD42" "YAGNI17" "ATM12" "ATM121"]}]) => {:gain 46 :path ["MONAD42" "LEGACY01" "META18"]})

(fact "Now real call"
  (optimize [{:DUREE 5 :PRIX 1 :VOL "F514" :DEPART 0}]) => {:gain 1 :path ["F514"]})

(fact
  (optimize [{:VOL "AF1" :DEPART 0 :DUREE 1 :PRIX 2}
             {:VOL "AF2" :DEPART 4 :DUREE 1 :PRIX 4}
             {:VOL "AF3" :DEPART 2 :DUREE 1 :PRIX 6}]) => {:gain 12 :path ["AF1" "AF3" "AF2"]})

(fact
  (optimize [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
             {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
             {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
             {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => {:gain 18 :path ["MONAD42" "LEGACY01"]})

(fact
  (optimize '({:VOL "anxious-roughneck-80", :DEPART 3, :DUREE 4, :PRIX 5}
              {:VOL "puzzled-wharf-81", :DEPART 3, :DUREE 9, :PRIX 15}
              {:VOL "quick-buddy-93", :DEPART 1, :DUREE 2, :PRIX 5}
              {:VOL "poor-ukulele-38", :DEPART 3, :DUREE 6, :PRIX 12}
              {:VOL "jealous-griddlecake-82", :DEPART 4, :DUREE 15, :PRIX 7})) => {:path ["quick-buddy-93" "puzzled-wharf-81"], :gain 20})

(comment
  (doseq [i (range 100)]
    (time
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
                {:VOL "hollow-photography-8"        :DEPART 24  :DUREE 5 :PRIX 12}]))))
