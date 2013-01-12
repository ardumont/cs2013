(ns cs2013.mail-test
  (:use [midje.sweet :only [fact]]
        [cs2013.mail])
  (:require [clojure.test :refer :all]))

(fact
 (my) => (apply str (reverse ["com" "." "gmail" "@" "t" "." "eniotna"])))
