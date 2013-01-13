(ns cs2013.operations-test
  (:use [midje.sweet]
        [cs2013.operations]))

(fact
 (map char2int "0123456789") => (range 0 10))

(fact
  (compute-operation "1+2")       => 3
  (compute-operation "1+2")       => 3
  (compute-operation "(1+2)")     => 3
  (compute-operation "(1+2)/2")   => 3/2
  (compute-operation "(1+2+3)")   => 6
  (compute-operation "(1+2+3+4+5+6+7+8+9)*3") => 135)

(fact
 (rational-2-decimal 3/2) => 1.5
 (rational-2-decimal 3)   => 3)
