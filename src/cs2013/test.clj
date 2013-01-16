(ns cs2013.test
  (:require [incanter.core :as i]))

; --------------------------------------------------------------------------------

(comment
  (defn in->candidates
    [in]
    (->> in
         (map (fn [c] {:path [c]
                      :cmds (remove (partial = c) in)}))))

  (defn candidate->children
    [candidate]
    (->> candidate
         :cmds
         (map (fn [cmd] {:path (conj (:path candidate) cmd)
                        :cmds (remove #{cmd} (:cmds candidate))}))))

  (defn candidates->candidates
    [candidates]
    (->> candidates
         (mapcat candidate->children)))

  (def all-mach
    (->> in
         in->candidates
         (iterate candidates->candidates))))
