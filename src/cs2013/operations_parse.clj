(ns cs2013.operations-parse)

(defn inject "Just inject the char into the world of parser"
  [c]
  (fn [in] [[c in]]))

(defn failure "Failure parser" [] (fn [_] []))

(defn item "parse one element"
  []
  (fn [in]
    (if-let [[x y] in]
      [[(first in) (rest in)]]
      [])))
