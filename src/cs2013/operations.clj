(ns ^{:doc "A namespace to deal with query operations"}
  cs2013.operations)

(defn char2int
  "Compute a char inside \"0123456789\" into its integer value (no checking the bad input)"
  [c]
  (- (int c) (int \0)))

(def ^{:private true
       :doc "Map of coding operations translations"}
  operators {\space +
             \* *
             \- -
             \/ /})

(defn compute-simple-operation
  "Compute a simple operation of the form 1*1"
  [q]
  (let [[a op b] q
        x (char2int a)
        y (char2int b)]
    ((operators op) x y)))

(defn compute-operation
  "Compute an operation of the (1*1)*10"
  [q]
  (let [[_ a op b _ op2 c] q
        x (char2int a)
        y (char2int b)
        z (char2int c)
        res (-> ((operators op) x y)
                ((operators op2) z))]
    res))
