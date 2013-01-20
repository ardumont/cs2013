(ns ^{:doc "A namespace for testing stuff"}
  cs2013.enonce2-2
    (:use [midje.sweet :only [fact future-fact truthy falsey]])
    (:require [cs2013.enonce2 :as e2]
              [clojure.tools.trace :only [trace] :as t]))

; --------------------------------------------------------------------------------

(defn in->candidates
  "Given a list of input commands, compute the map of :path/:cmds (first level)"
  [input]
  (concat
   (->> input
        (map (fn [c] {:path [c] :cmds (remove #{c} input)})))
   (->> input
        (map (fn [c] {:path [c] :cmds ()})))))

(defn valid?
  "Are all the commands valid?"
  [cand]
  (every? true?
        (map (fn [{:keys [DEPART DUREE]} next-node] (<= (+ DEPART DUREE) (:DEPART next-node)))
             cand
             (rest cand))))

(defn candidate->commands
  "Compute the list of children of one candidate"
  [{:keys [path gain cmds] :as cand}]
  (filter
   (comp valid? :path)
   (concat
    (->> cmds
         (map (fn [c] {:path (conj path c)
                      :cmds (remove #{c} cmds)})))
    (->> cmds
         (map (fn [c] {:path (conj path c)
                      :cmds ()}))))))

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
     (-> m
         (assoc-in [:gain] (+ gain PRIX))
         (assoc-in [:path] (conj path VOL))))
   {:gain 0
    :path []}
   (:path cmd)))

(defn all-valid-commands
  "Compute all possible matches."
  [in]
  (->> in
       e2/sort-by-duration
       in->candidates
       (iterate candidates->candidates)
       (take-while (comp not empty?))
       last
       e2/best-paths
       first
       compute-result))
