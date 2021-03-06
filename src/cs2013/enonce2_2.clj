(ns ^{:doc "Another approach for solving the second problem"}
  cs2013.enonce2-2
    (:use [midje.sweet :only [fact future-fact truthy falsey]])
    (:require [clojure.tools.trace :only [trace] :as t]))

(defn sort-by-duration
  "Sort a vector of maps with the key :DEPART"
  [vmap-path]
  (->> vmap-path (sort-by :DEPART) vec))

(defn invalid?
  "Is the new command cmd invalid regarding the reference command cmd (overlap between DEPART)"
  [{:keys [DEPART DUREE]} cmd]
  (< (:DEPART cmd) (+ DEPART DUREE)))

(defn in->candidates
  "Given a list of input commands, compute the map of :path/:cmds (first level)"
  [input]
  (->> input
       (mapcat (fn [c]
                 [{:path [c]
                   :cmds (remove #(invalid? c %) input)}
                  {:path [c] :cmds ()}]))))

(defn candidate->commands
  "Compute the list of children of one candidate"
  [{:keys [path gain cmds] :as cand}]
  (->> cmds
       (mapcat (fn [c]
                 (let [npath (conj path c)]
                   [{:path npath
                     :cmds (remove #(invalid? c %) cmds)}
                    {:path npath
                     :cmds ()}])))))

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

(defn best-paths
  "Compute the best paths from a list of path"
  [gain-paths]
  (let [possible-solutions (group-by :gain gain-paths)
        best (->> possible-solutions keys (apply max))]
    (possible-solutions best)))

(defn optimize
  "Compute all possible matches."
  [in]
  (->> in
       sort-by-duration
       in->candidates
       (iterate candidates->candidates)
       (take-while (comp not empty?))
       last
       (map compute-result)
       best-paths
       first))
