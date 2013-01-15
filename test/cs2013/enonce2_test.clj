(ns cs2013.enonce2-test
  (:use [midje.sweet]
        [cs2013.enonce2]))

(fact "Dummy fact"
  (optimize [{:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
             {:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
             {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
             {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => {:gain 18 :path ["MONAD42" "LEGACY01"]})
