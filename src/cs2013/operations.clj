(ns ^{:doc "A namespace to deal with query operations"}
  cs2013.operations
  (:require [clojure.tools.trace :only [trace] :as t]))

(defn compute "Compute"
  [x & r]
  (reduce (fn [e [op l]] (op e l)) x (partition 2 r)))

(def ^{:doc "Map of coding operations translations"}
  operators {\space +
             \+ +
             \* *
             \- -
             \/ /})

(defn to-int
  [s]
  (->> s (clojure.string/join "") read-string))

(defn map-char-and-operator-to-real
  "Replace all digits and operators by their numbers and operations equivalent"
  [s]
  (loop [acc [] c (first s) r (rest s)]
    (if (nil? c)
      acc
      (cond (#{\* \+ \- \/} c) (recur (conj acc (operators c)) (first r) (next r))
            :else              (let [num    (->> r (take-while (fn [e] (not (#{\* \+ \/ -} e)))) (cons c) to-int)
                                     new-r  (drop-while (fn [e] (not (#{\* \+ \/ -} e)))  r)]
                                 (recur (conj acc num) (first new-r) (rest new-r)))))))

(defn int2char
  "integer to character"
  [n]
  (char (+ n (int \0))))

(defn char2int
  "Compute a char inside \"0123456789\" into its integer value (no checking the bad input)"
  [c]
  (- (int c) (int \0)))

(defn compute-acc
  [acc]
  (->> acc reverse map-char-and-operator-to-real (apply compute)))

(defn opstr-2-opdigit
  "Filter out the digits into number"
  [s]
  (loop [acc [] c (first s) r (rest s)]
    (if (nil? c)
      (if (seq? acc)
        (compute-acc acc)
        acc)
      (cond (#{\)} c)             (let [res     (->> acc (take-while (fn [e] (not (#{\(} e)))) compute-acc)
                                        new-acc (->> acc (drop-while (fn [e] (not (#{\(} e)))) next)]
                                    (recur (cons res new-acc) (first r) (next r)))
            :else                  (recur (cons c acc) (first r) (next r))))))

(defn rational-2-decimal
  "Transform a rational into decimal"
  [n]
  (if (integer? n) n (float n)))

(defn compute-infix-operation-from-string
  "Main entry point to compute an infix string operation"
  [s]
  (-> s
      opstr-2-opdigit    ;; transforming digits char into digits
      rational-2-decimal)) ;; expected decimal and not rational, but integer for the rest
