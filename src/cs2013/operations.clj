(ns ^{:doc "A namespace to deal with query operations"}
  cs2013.operations
  (:require [clojure.tools.trace :only [trace] :as t]
            [clojure.string :as str]))

(defn compute "Compute an infix operation (no parenthesis, no precedence, just a basic foldl computation)"
  [x & r]
  (reduce (fn [e [op l]] (op e l)) x (partition 2 r)))

(def ^{:private true
       :doc "Map of coding operations translations"}
  operators {\space +
             \+ +
             \* *
             \- -
             \/ /})

(def ^{:private true
       :doc "To not desynchronize from the operators's map"}
  keys-operators (-> operators keys set))

(defn to-int
  ""
  [s]
  (->> s (clojure.string/join "") read-string))

(defn map-char-and-operator-to-real
  "Replace all digits and operators by their numbers and operations equivalent"
  [s]
  (loop [acc [] c (first s) r (rest s)]
    (if (nil? c)
      acc
      (cond (#{\* \+ \- \/} c) (recur (conj acc (operators c)) (first r) (next r))
            :else              (let [num    (->> r (take-while (fn [e] (not (keys-operators e)))) (cons c) to-int)
                                     new-r  (drop-while (fn [e] (not (keys-operators e)))  r)]
                                 (recur (conj acc num) (first new-r) (rest new-r)))))))

(defn int2char
  "integer to character"
  [n]
  (char (+ n (int \0))))

(defn char2int
  "Compute a char inside \"0123456789\" into its integer value (no checking the bad input)"
  [c]
  (- (int c) (int \0)))

(defn- compute-acc
  "Factorized code to deal with the computed of the accumulator in a certain state"
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
  (cond (integer? n)  n
        (zero? (- (int n) n)) (int n)
        :else (float n)))

(defn compute-infix-operation-from-string
  "Main entry point to compute an infix string operation"
  [s]
  (-> s
      opstr-2-opdigit      ;; transforming digits char into digits
      rational-2-decimal)) ;; expected decimal and not rational, but integer for the rest

;; other tryout - largely inspired from http://rosettacode.org/wiki/Arithmetic_evaluation#Clojure

(def precedence '{* 0, / 0
                  + 1, - 1})

(defn order-ops
  "((A x B) y C) or (A x (B y C)) depending on precedence of x and y"
  [[A x B y C & more]]
  (let [ret (if (<=  (precedence x)
                     (precedence y))
              (list (list A x B) y C)
              (list A x (list B y C)))]
    (if more
      (recur (concat ret more))
      ret)))

(defn add-parens
  "Tree walk to add parens. All lists are length 3 afterwards."
  [s]
  (clojure.walk/postwalk
   (fn [e] (if (seq? e)
           (let [c (count e)]
             (cond (even? c) (throw (Exception. "Must be an odd number of forms"))
                   (= c 1) (first e)
                   (= c 3) e
                   (>= c 5) (order-ops e)))
           e))
   s))

(defn make-ast
  "Parse a string into a list of numbers, ops, and lists"
  [s]
  (-> (format "'(%s)" s)                ;; first wrap the call as a list
      (str/replace #"([*+-/])" " $1 ")  ;; make space between operators, needed for the add-parens call
      (.replaceAll " [\\.,] " ".")      ;; deal with decimal
      load-string                       ;; load just the form using clojure (no eval)
      add-parens))                      ;; add parenthesis by pair using order precedence

(def ^{:doc "operator transco"}
  ops {'* *
       '+ +
       '- -
       '/ /})

(defn eval-ast
  "Postfix Evaluation of an infix operation well parenthesized (A op B)"
  [ast]
  (clojure.walk/postwalk
   (fn [e]
     (if (seq? e)
       (let [[a o b] e] ((ops o) a b))
       e))
   ast))

(defn evaluate [s]
  "Parse and evaluate an infix arithmetic expression"
  (-> s make-ast eval-ast))

(defn compute-infix-operation-from-string
  "Main entry point to compute an infix string operation"
  [s]
  (-> s
      evaluate             ;; transforming digits char into digits
      rational-2-decimal)) ;; expected decimal and not rational, but integer for the rest
