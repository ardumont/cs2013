(ns cs2013.web-test
  (:use [midje.sweet :only [fact]]
        [cs2013.web]))

(fact
 (deal-with-query "Quelle est ton adresse email") => {:status 200
                                                      :headers {"Content-Type" "text/plain"}
                                                      :body (apply str (reverse ["com" "." "gmail" "@" "t" "." "eniotna"]))}

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

 ;; this method does not support between one digit, one operator, one digit
 (deal-with-query "1+1") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "2"}

 (deal-with-query "9+9") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "18"}

 (deal-with-query "1*1") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "1"}

 (deal-with-query "9*9") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "81"}

 (deal-with-query "2/2") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "1"}

 (deal-with-query "9/9") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "1"}

 (deal-with-query "2-9") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "-7"}

 (deal-with-query "9-5") => {:status 200
                             :headers {"Content-Type" "text/plain"}
                             :body "4"})

(fact
 (deal-with-query "(1*2)*2") => {:status 200
                                 :headers {"Content-Type" "text/plain"}
                                 :body "4"})

(fact
 (deal-with-query "(1+2)/2") => {:status 200
                                 :headers {"Content-Type" "text/plain"}
                                 :body "1,5"})
