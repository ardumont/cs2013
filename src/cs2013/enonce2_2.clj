(ns ^{:doc "Another approach for solving the second problem"}
  cs2013.enonce2-2
    (:use [midje.sweet :only [fact future-fact truthy falsey]])
    (:require [cs2013.enonce2 :as e2]
              [clojure.tools.trace :only [trace] :as t]))

(defn invalid?
  "Is the new command cmd invalid regarding the reference command cmd (overlap between DEPART)"
  [{:keys [DEPART DUREE]} cmd]
  (< (:DEPART cmd) (+ DEPART DUREE)))

(defn in->candidates
  "Given a list of input commands, compute the map of :path/:cmds (first level)"
  [input]

  (->> input
       (mapcat (fn [{:keys [DEPART DUREE] :as c}]
                 [{:path [c]
                   :cmds (remove #(invalid? c %) input)}
                  {:path [c] :cmds ()}]))))

(defn candidate->commands
  "Compute the list of children of one candidate"
  [{:keys [path gain cmds] :as cand}]
  (->> cmds
       (mapcat (fn [c]
                 [{:path (conj path c)
                   :cmds (remove #(invalid? c %) cmds)}
                  {:path (conj path c)
                    :cmds ()}]))))

(defn candidates->candidates
  "Compute all the candidates from the list of candidates"
  [cands]
  (->> cands
       (mapcat candidate->commands)))

(defn compute-result
  "Given a command, compute the output"
  [cmd]
  (reduce
   (fn [{:keys [gain path] :as m} {:keys [PRIX VOL]}]
     {:gain (+ gain PRIX)
      :path (conj path VOL)})
   {:gain 0 :path []}
   (:path cmd)))

(defn optimize
  "Compute all possible matches."
  [in]
  (->> in
       e2/sort-by-duration
       in->candidates
       (iterate candidates->candidates)
       (take-while (comp not empty?))
       last
       (map compute-result)
       e2/best-paths
       first))
