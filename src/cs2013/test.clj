(ns ^{:doc "A namespace for testing stuff"} cs2013.test
  (:use [midje.sweet :only [fact future-fact truthy falsey]]))

; --------------------------------------------------------------------------------

(def ^{:doc "input for tests"}
  in [{:VOL "MONAD42"  :DEPART 0 :DUREE 3 :PRIX 10}
      {:VOL "META18"   :DEPART 3 :DUREE 1 :PRIX 14}
      {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}])

(defn in->candidates
  "Given a list of input commands, compute the map of :path/:cmds (first level)"
  [in]
  (->> in
       (map (fn [c] {:path [c] :cmds (remove #{c} in)}))))

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

(defn valid?
  "Filter invalid commands"
  [cand]
  (every? true?
        (map (fn [{:keys [DEPART DUREE]} next-node] (<= (+ DEPART DUREE) (:DEPART next-node)))
             cand
             (rest cand))))

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

(defn candidate->commands
  "Compute the list of children of one candidate"
  [{:keys [path gain cmds] :as cand}]
  (->> cmds
       (map (fn [c] {:path (conj path c)
                    :cmds (remove #{c} cmds)}))
       (filter (comp valid? :path))))

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

(defn candidates->candidates
  "Compute all the candidates from the list of candidates"
  [cands]
  (->> cands
       (mapcat candidate->commands)))

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

(defn sort-by-duration
  "Sort a vector of maps with the key :DEPART"
  [vmap-path]
  (->> vmap-path (sort-by :DEPART) vec))

(fact "sorting"
  (sort-by-duration [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                     {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                     {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                     {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => [{:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                                                                        {:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                                                                        {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                                                                        {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}])

(defn all-valid-commands
  "Compute all possible matches."
  [in]
  (->> in
       sort-by-duration
       in->candidates
       (iterate candidates->candidates)
       (take-while (comp not empty?))
       last))
