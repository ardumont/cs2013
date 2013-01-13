(ns ^{:doc "A namespace to deal with query operations"}
  cs2013.operations
  (:require [clojure.tools.trace :only [trace] :as t]))

(defn char2int
  "Compute a char inside \"0123456789\" into its integer value (no checking the bad input)"
  [c]
  (- (int c) (int \0)))

(def ^{:doc "Map of coding operations translations"}
  operators {\space +
             \+ +
             \* *
             \- -
             \/ /})

(defn compute-operation
  "Compute the operations (possible improvement: separation between parsing and computing)"
  [s]
  (->> s
       (reduce
        (fn [acc c]
          (cond
           ;; parenthesis closed, we retrieve the first element which is the value, the second is the dual parenthesis (hypothesis: expression well balanced)
           (= \) c)              (cons (first acc) (drop 2 acc))
           ;; the operators and the opening parenthesis are simply conj-ed onto the accumulator
           (#{\* \- \+ \/ \(} c) (cons c acc)
           ;; 2 cases, either the first element is an operator, then we need to compute with the char c, either we just conj this car to the accumulator
           :else                 (let [p (first acc)]
                                   (if-let [op (operators p)]
                                     (let [a (second acc)
                                           b (char2int c)
                                           val (op a b)]
                                       (cons val (drop 2 acc)))
                                     (cons (char2int c) acc)))))
        [])
       first))

(defn rational-2-decimal
  "Transform a rational into decimal"
  [n]
  (if (and ((comp not integer?) n)
           (rational? n))
    (float n)
    n))
