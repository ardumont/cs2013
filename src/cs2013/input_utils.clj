(ns ^{:doc "Some utility to rework with the web input"}
  cs2013.input-utils)

(defn keyify-map
  "Tranform keys of map into keyword"
  [m]
  (into {} (for [[k v] m] [(keyword k) v])))

(defn keyify
  "Tranform vectors of maps into a vector of keyified maps"
  [v]
  (map keyify-map v))
