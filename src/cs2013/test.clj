(ns ^{:doc "A namespace for testing stuff"} cs2013.test
  (:use [midje.sweet :only [fact future-fact]]))

; --------------------------------------------------------------------------------

(def ^{:doc "input for tests"}
  in [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
      {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
      {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
      {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}])

(defn in->candidates
  "Given a list of input commands, compute the map of :path/:cmds (first level)"
  [in]
  (->> in
       (map (fn [c] {:path [c]
                    :cmds (remove #{c} in)}))))

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

(def ^{:doc "Input for tests"}
  candidate
  '{:path [{:VOL "META18"}]
    :cmds ({:VOL "LEGACY01"} {:VOL "MONAD42"} {:VOL "YAGNI17"})})

(defn candidate->children
  "Compute the list of children of one candidate"
  [{:keys [path gain cmds] :as cand}]
  (map (fn [c] {:path (conj path c)
               :cmds (remove #{c} cmds)}) cmds))

(fact "Compute the children of one candidate"
  (candidate->children {:path [{:VOL "META18"}]
                        :cmds '({:VOL "LEGACY01"}
                                {:VOL "MONAD42"}
                                {:VOL "YAGNI17"})}) => '[{:path [{:VOL "META18"} {:VOL "LEGACY01"}]
                                                          :cmds ({:VOL "MONAD42"} {:VOL "YAGNI17"})}
                                                         {:path [{:VOL "META18"} {:VOL "MONAD42"}]
                                                          :cmds ({:VOL "LEGACY01"} {:VOL "YAGNI17"})}
                                                         {:path [{:VOL "META18"} {:VOL "YAGNI17"}]
                                                          :cmds ({:VOL "LEGACY01"} {:VOL "MONAD42"})}])

(def ^{:doc "Compute all the candidates from the list of candidates"}
  candidates->candidates (partial mapcat candidate->children))

(defn all-match
  "Compute all the possible matches"
  [in]
  (->> in
       in->candidates
       (iterate candidates->candidates)
       (take-while (comp seq :cmds))))
