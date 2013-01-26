(ns ^{:doc "The second problem received via post request + solutions - third implementation"}
  cs2013.enonce2-3
  (:require [clojure.tools.trace :only [trace] :as t]))

(defn adjacents
  "Compute the adjacent node starting at depart."
  [depart mnodes]
  (if-let [res (subseq mnodes >= depart)]
    (-> res first val)))

(defn optimize
  "Compute the best command that optimizes the gain."
  [cmds]
  (->> cmds
       (group-by (fn [{:keys [DEPART DUREE]}] (+ DEPART DUREE)))
       (sort-by key)
       (reduce
        (fn [maxima [next-depart root-nodes]]
          (let [max (-> maxima first val)]
            (assoc maxima next-depart
                   (apply max-key :gain
                          max
                          (for [{:keys [PRIX VOL DEPART]} root-nodes
                                :let [{:keys [gain path]} (adjacents DEPART maxima)]]
                            {:gain (+ gain PRIX)
                             :path (conj path VOL)})))))
        (sorted-map-by > 0 {:gain 0 :path []}))
       first
       val))
