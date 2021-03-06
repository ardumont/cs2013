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
  (opstr-2-opdigit "1-2")                                => -1
  (opstr-2-opdigit "1+2*10")                             => 30
  (opstr-2-opdigit "(1+2+10)")                           => 13
  (opstr-2-opdigit "(1+2+10)*3")                         => 39
  (opstr-2-opdigit "(1+2+10)*30")                        => 390
  (opstr-2-opdigit "((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5") => 825/2)

(fact
  (compute-infix-operation-from-string "1+2*10")                                                                                 => 21
  (compute-infix-operation-from-string "1.5+2*10")                                                                               => 21.5
  (compute-infix-operation-from-string "1.5*4")                                                                                  => 6
  (compute-infix-operation-from-string "1,5*4")                                                                                  => 6
  (compute-infix-operation-from-string "(1+2+10)")                                                                               => 13
  (compute-infix-operation-from-string "(1+2+10)*3")                                                                             => 39
  (compute-infix-operation-from-string "(1+2+10)*30")                                                                            => 390
  (compute-infix-operation-from-string "(3+3+4+18+27*3)/2*5")                                                                    => 272.5
  (compute-infix-operation-from-string "((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5")                                                     => 272.5
  (compute-infix-operation-from-string "((1,1+2)+3,14+4+(5+6+7)+(8+9+10)*4267387833344334647677634)/2*553344300034334349999000") => 31878018903828899277492024491376690701584023926880
  (compute-infix-operation-from-string "(-1)+(1)")                                                                               => 0
  (compute-infix-operation-from-string "(-1+(-1))")                                                                              => -2
  (compute-infix-operation-from-string "9-7")                                                                                    => 2
  (compute-infix-operation-from-string "7-9")                                                                                    => -2)

(fact
  (compute 2 + 5)                           => 7
  (compute 38 + 48 - 2 / 2)                 => 42
  (compute 10 / 2 - 1 * 2)                  => 8
  (compute 20 / 2 + 2 + 4 + 8 - 6 - 10 * 9) => 72)

(fact
 (rational-2-decimal 3/2) => 1.5
 (rational-2-decimal 3)   => 3
 (rational-2-decimal 3/1) => 3
 (rational-2-decimal 3.0) => 3)

(fact
  (make-ast "1+2*10")                                                                                 => '(1 + (2 * 10))
  (make-ast "1.5+2*10")                                                                               => '(1.5 + (2 * 10))
  (make-ast "1,5+2*10")                                                                               => '(1.5 + (2 * 10))
  (make-ast "1+2+10")                                                                                 => '((1 + 2) + 10)
  (make-ast "(1+2+10)")                                                                               => '((1 + 2) + 10)
  (make-ast "((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5")                                                     => '(((((((1 + 2) + 3) + 4) + ((5 + 6) + 7)) + (((8 + 9) + 10) * 3)) / 2) * 5)
  (make-ast "((1,1+2)+3,14+4+(5+6+7)+(8+9+10)*4267387833344334647677634)/2*553344300034334349999000") => '(((((((1.1 + 2) + 3.14) + 4) + ((5 + 6) + 7)) + (((8 + 9) + 10) * 4267387833344334647677634N)) / 2) * 553344300034334349999000N)
  (make-ast "(-1)+(1)") => '(-1 + 1)
  (make-ast "9-7") => '(9 - 7)
  (make-ast "-9-7") => '(-9 - 7))

(fact
  (eval-ast '(1 + (2 * 10)))                                                             => 21
  (eval-ast '(1.5 + (2 * 10)))                                                           => 43/2
  (eval-ast '((1 + 2) + 10))                                                             => 13
  (eval-ast '(((((((1 + 2) + 3) + 4) + ((5 + 6) + 7)) + (((8 + 9) + 10) * 3)) / 2) * 5)) => 545/2
  (eval-ast '(((((((1.1 + 2) + 3.14) + 4) + ((5 + 6) + 7)) + (((8 + 9) + 10) * 4267387833344334647677634N)) / 2) * 553344300034334349999000N)) => 31878018903828899277492024491376690701584023926880N
  (eval-ast '(9 - 7))                                                                   => 2
  (eval-ast '(-9 - 7))                                                                   => -16
  (eval-ast '(-1 + 1))                                                                   => 0)
