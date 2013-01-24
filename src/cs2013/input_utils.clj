(ns ^{:doc "Some utility to rework with the web input"}
  cs2013.input-utils)

(defn keyify
  "Tranform vectors of maps into a vector of keyified maps"
  [v]
  (map clojure.walk/keywordize-keys v))
