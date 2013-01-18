(ns cs2013.web-test
  (:use [midje.sweet :only [fact]]
        [cs2013.web]))

(fact "Simple get"
  (deal-with-query "Quelle est ton adresse email")                                                             => (apply str (reverse ["com" "." "gmail" "@" "t" "." "eniotna"]))
  (deal-with-query "Es tu abonne a la mailing list(OUI/NON)")                                                  => "OUI"
  (deal-with-query "Es tu heureux de participer(OUI/NON)")                                                     => "OUI"
  (deal-with-query "Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)")               => "OUI"
  (deal-with-query "Est ce que tu reponds toujours oui(OUI/NON)")                                              => "NON"
  (deal-with-query "As tu bien recu le premier enonce(OUI/NON)")                                               => "OUI"
  (deal-with-query "As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)") => "QUELS_BUGS"
  (deal-with-query "As tu bien recu le second enonce(OUI/NON)")                                                => "OUI"
  (deal-with-query "As tu copie le code de ndeloof(OUI/NON/JE_SUIS_NICOLAS)")                                  => "NON")

(fact "simple"
   (deal-with-query "1+1")                                                                                     => "2"
   (deal-with-query "9+9")                                                                                     => "18"
   (deal-with-query "1*1")                                                                                     => "1"
   (deal-with-query "9*9")                                                                                     => "81"
   (deal-with-query "2/2")                                                                                     => "1"
   (deal-with-query "9/9")                                                                                     => "1"
   (deal-with-query "2-9")                                                                                     => "-7"
   (deal-with-query "9-5")                                                                                     => "4")

(fact "more complex one"
  (deal-with-query "(1*2)*2")                                                                                  => "4"
  (deal-with-query "(1+2+3+4+5+6+7+8+9+10)*2")                                                                 => "110"
  (deal-with-query "(1+2)/2")                                                                                  => "1,5"
  (deal-with-query "1.5*4")                                                                                    => "6"
  (deal-with-query "1,5*4")                                                                                    => "6"
  (deal-with-query "((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5")                                                       => "272,5"
  (deal-with-query "((1,1+2)+3,14+4+(5+6+7)+(8+9+10)*4267387833344334647677634)/2*553344300034334349999000")   => "31878018903828899277492024491376690701584023926880"
  (deal-with-query "(-1)+(1)")                                                                                 => "0")
