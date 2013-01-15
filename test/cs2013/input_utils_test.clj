(ns cs2013.input-utils-test
  (:use [cs2013.input-utils]
        [midje.sweet]))

(fact
 (keyify-map {"DUREE" 5 "PRIX" 10 "VOL" "MONAD42" "DEPART" 0}) => {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10})

(fact
 (keyify [{"DUREE" 5  "PRIX" 10  "VOL" "MONAD42"  "DEPART" 0}
          {"DUREE" 7  "PRIX" 14  "VOL" "META18"  "DEPART" 3}
          {"DUREE" 9  "PRIX" 8  "VOL" "LEGACY01"  "DEPART" 5}
          {"DUREE" 9  "PRIX" 7  "VOL" "YAGNI17"  "DEPART" 5}]) => [{:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                                                                   {:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                                                                   {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                                                                   {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}])
