(ns cs2013.operations-parse)

(defn inject
  "Just inject the char into the world of parser"
  [c]
  (fn [in] [[c in]]))

(defn failure "Failure parser" [] (fn [_] []))

(defn item
  "parse one element, [] if null or empty, else parse the first element"
  []
  (fn [in]
    (if (or (nil? in) (empty? in))
      []
      (let [[x y] in]
        [[(first in) (rest in)]]))))

(defn parse "Function that takes a parser and apply it"
  [p in]
  (p in))
