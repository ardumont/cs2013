(ns cs2013.operations-test
  (:use [midje.sweet]
        [cs2013.operations]))

(fact
 (map char2int "0123456789") => (range 0 10))

(fact
 (map int2char (range 0 10)) => '(\0 \1 \2 \3 \4 \5 \6 \7 \8 \9))

(future-fact
 (compute-operation "1+2")       => 3
 (compute-operation "1+2")       => 3
 (compute-operation "(1+2)")     => 3
 (compute-operation "(1+2)/2")   => 3/2
 (compute-operation "(1+2+3)")   => 6
 (compute-operation "(1+2+3+4+5+6+7+8+9)*3") => 135
 (compute-operation "(1+2+3+4+5+6+7+8+9+10)*2") => 110)

(fact
  (to-int '(\1 \0)) => 10
  (to-int '(\2 \9 \8)) => 298)

(fact
  (opstr-2-opdigit "(1+2+10)*3") => '( \( 1 \+ 2 \+ 10 \) \* 3)
  (opstr-2-opdigit "(1+2+10)*30") => '( \( 1 \+ 2 \+ 10 \) \* 30))

(fact
 (rational-2-decimal 3/2) => 1.5
 (rational-2-decimal 3)   => 3)
