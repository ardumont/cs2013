(ns cs2013.operations-test
  (:use [midje.sweet :only [fact]]
        [cs2013.operations]))

(fact "compute-simple-operation - only simple expression, space is +, one digit, only support for + *"
   (compute-simple-operation "1 1") => 2
   (compute-simple-operation "1*1") => 1
   (compute-simple-operation "9*9") => 81)

(fact "compute-operation - beware the current implementation are limited to such format (one digit only too) "
   (compute-operation "(1 2) 9") => 12
   (compute-operation "(1 2)*9") => 27
   (compute-operation "(1 2)/2") => 1.5)

(fact
 (map char2int "0123456789") => (range 0 10))
