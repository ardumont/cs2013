(ns cs2013.operations-test
  (:use [midje.sweet]
        [cs2013.operations]))

(fact
 (map char2int "0123456789") => (range 0 10))

(fact
 (map int2char (range 0 10)) => '(\0 \1 \2 \3 \4 \5 \6 \7 \8 \9))

(fact
  (to-int '(\1 \0)) => 10
  (to-int '(\2 \9 \8)) => 298)

(fact
  (opstr-2-opdigit "1+2*10")                             => 30
  (opstr-2-opdigit "(1+2+10)")                           => 13
  (opstr-2-opdigit "(1+2+10)*3")                         => 39
  (opstr-2-opdigit "(1+2+10)*30")                        => 390
  (opstr-2-opdigit "((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5") => 825/2)

(future-fact
 (compute-infix-operation-from-string "1+2*10")              => 30
 (compute-infix-operation-from-string "(1+2+10)")            => 13
 (compute-infix-operation-from-string "(1+2+10)*3")          => 39
 (compute-infix-operation-from-string "(1+2+10)*30")         => 390
 (compute-infix-operation-from-string "(3+3+4+18+27*3)/2*5") => 412.5)

(future-fact
  (compute-infix-operation-from-string "((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5") => 412.5)

(fact
  (compute 2 + 5)                           => 7
  (compute 38 + 48 - 2 / 2)                 => 42
  (compute 10 / 2 - 1 * 2)                  => 8
  (compute 20 / 2 + 2 + 4 + 8 - 6 - 10 * 9) => 72)

(fact
 (rational-2-decimal 3/2) => 1.5
 (rational-2-decimal 3)   => 3)

;; (fact
;;   (map-char-and-operator-to-real [\1 \+ \2 \+ \3 \0]) => '(1 :+ 2 :+ 30)
;;   (provided
;;     (operators \1) => nil
;;     (operators \2) => nil
;;     (operators \3) => nil
;;     (operators \+) => :+))
