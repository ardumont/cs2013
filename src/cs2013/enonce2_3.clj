(ns ^{:doc "The second problem received via post request + solutions - third implementation"}
  cs2013.enonce2-3
  (:require [clojure.tools.trace :only [trace] :as t]))

(comment
  (-> group-and-sort (subseq > 250) pprint))

(defn build-tree
  "breadth-first lazily building the tree"
  [adjs root-node]
  ((fn next-elt [queue]
     (lazy-seq
      (when (seq queue)
        (let [{:keys [id gain path] :as node} (peek queue)
              {:keys [DEPART DUREE]} (:cmd node)
              children (for [[_ cmds] (subseq adjs > (+ DEPART DUREE))
                             {:keys [VOL PRIX] :as cmd} cmds]
                         {:cmd cmd
                          :gain (+ gain PRIX)
                          :path (conj path VOL)})]
          (cons node (->> children
                          (into (pop queue))
                          next-elt))))))
   (conj clojure.lang.PersistentQueue/EMPTY root-node)))

(defn build-tree-root
  "Begin the building of the tree"
  [adjs {:keys [VOL PRIX] :as node}]
  (build-tree adjs {:cmd node
                    :gain PRIX
                    :path [VOL]}))

(defn best-path
  "Compute the best paths from a list of path"
  [gain-paths]
  (let [fbest (apply max-key :gain gain-paths)]
    {:gain (:gain fbest)
     :path (:path fbest)}))

(defn optimize
  "Compute the commands that optimize the gain."
  [cmds]
  (let [adjs-sorted-map (->> cmds (group-by #(+ (:DEPART %) (:DUREE %))) (into (sorted-map)))]
    (->> cmds
         (mapcat (partial build-tree-root adjs-sorted-map))
         best-path)))
