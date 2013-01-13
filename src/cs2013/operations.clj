(ns ^{:doc "A namespace to deal with query operations"}
  cs2013.operations
  (:require [clojure.tools.trace :only [trace] :as t]))

(defn compute "Compute"
  [x & r]
  (reduce (fn [e [op l]] (op e l)) x (partition 2 r)))

(defn int2char
  "integer to character"
  [n]
  (char (+ n (int \0))))

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
(defn to-int
  [s]
  (->> s (clojure.string/join "") read-string))

(defn opstr-2-opdigit
  "Filter out the digits into number"
  [s]
  (reverse
   (loop [acc [] c (first s) r (rest s)]
     (if (nil? c)
       (if (= \) (first acc))
         acc
         (let [number  (->> acc (take-while (fn [c] (not (#{\* \- \+ \/ \(} c)))) reverse to-int)
               new-acc (->> acc (drop-while (fn [c] (not (#{\* \- \+ \/ \(} c)))) (cons number))]
           new-acc))
       (cond (#{\* \- \+ \/ \)} c) (if (= \) (first acc))
                                     ;; the first element of the accumulator is (, then we only cons the operator
                                     (recur (cons c acc) (first r) (next r))
                                     ;; otherwise, some savant extracting to transform into numbers and keeping the operations
                                     (let [number  (->> acc (take-while (fn [c] (not (#{\* \- \+ \/ \(} c)))) reverse to-int)
                                           new-acc (->> acc (drop-while (fn [c] (not (#{\* \- \+ \/ \(} c)))) (cons number) (cons c))]
                                       (recur new-acc (first r) (next r))))
             :else                  (recur (cons c acc) (first r) (next r)))))))

(defn compute-operation
  "Compute the operations (sequence of operators in chars"
  [seq]
  (->> seq
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
                                           val (op a c)]
                                       (cons val (drop 2 acc)))
                                     (cons c acc)))))
        [])
       first))

(defn rational-2-decimal
  "Transform a rational into decimal"
  [n]
  (if (integer? n) n (float n)))

(defn compute-infix-operation-from-string
  "Main entry point to compute an infix string operation"
  [s]
  (-> s
      opstr-2-opdigit    ;; transforming digits char into digits
      compute-operation  ;; computing the infix operation
      rational-2-decimal)) ;; expected decimal and not rational
