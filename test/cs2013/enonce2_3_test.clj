(ns cs2013.enonce2-3-test
  (:use [midje.sweet]
        [cs2013.enonce2-3]
        [clojure.tools.trace :only [trace] :as t]))

(fact
  (optimize [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
             {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
             {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
             {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => {:gain 18 :path ["MONAD42" "LEGACY01"]})
