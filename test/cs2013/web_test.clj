(ns cs2013.web-test
  (:use [midje.sweet :only [fact]])
  (:require [clojure.test :refer :all]
            [cs2013.web :refer :all]))

(fact
 (deal-with-query "Quelle est ton adresse email") => {:status 200
                                                      :headers {"Content-Type" "text/plain"}
                                                      :body (str "eniotna" "." "t" "@" "gmail" "." "com")}

 (deal-with-query "Es tu abonne a la mailing list(OUI/NON)") => {:status 200
                                                                 :headers {"Content-Type" "text/plain"}
                                                                 :body "OUI"}
 (deal-with-query "Es tu heureux de participer(OUI/NON)") => {:status 200
                                                              :headers {"Content-Type" "text/plain"}
                                                              :body "OUI"}
 (deal-with-query "Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)") => {:status 200
                                                                                                    :headers {"Content-Type" "text/plain"}
                                                                                                    :body "OUI"}
 (deal-with-query "Est ce que tu reponds toujours oui(OUI/NON)") => {:status 200
                                                                     :headers {"Content-Type" "text/plain"}
                                                                     :body "NON"}
 (deal-with-query "As tu bien recu le premier enonce(OUI/NON)")  => {:status 200
                                                                     :headers {"Content-Type" "text/plain"}
                                                                     :body "OUI"}
 (deal-with-query "1 1") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "2"}

 (deal-with-query "10 22") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "32"})
