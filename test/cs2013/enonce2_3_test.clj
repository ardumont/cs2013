(ns cs2013.enonce2-3-test
  (:use [midje.sweet]
        [cs2013.enonce2-3]
        [clojure.tools.trace :only [trace] :as t]))

(fact
  (adjacents 5 (sorted-map 3 {:gain 10 :path [:a]}
                           0 {:gain 20 :path [:b :c]}
                           12 {:gain 40 :path [:d]})) => {:gain 40 :path [:d]})

(fact
  (adjacents 15 (sorted-map 3 {:gain 10 :path [:a]}
                            0 {:gain 20 :path [:b :c]}
                            12 {:gain 40 :path [:d]})) => nil)

(fact
  (adjacents 13 (sorted-map 3 {:gain 10 :path [:a]}
                            0 {:gain 20 :path [:b :c]}
                            12 {:gain 40 :path [:d]})) => nil)

(fact
   (optimize [{:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
              {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
              {:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
              {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}]) => {:gain 18 :path ["MONAD42" "LEGACY01"]})

(fact "Now real call"
  (optimize [{:DUREE 5 :PRIX 1 :VOL "F514" :DEPART 0}]) => {:gain 1 :path ["F514"]})

(fact
  (optimize [{:VOL "AF1" :DEPART 0 :DUREE 1 :PRIX 2}
             {:VOL "AF2" :DEPART 4 :DUREE 1 :PRIX 4}
             {:VOL "AF3" :DEPART 2 :DUREE 1 :PRIX 6}]) => {:gain 12 :path ["AF1" "AF3" "AF2"]})

(fact
  (optimize '({:VOL "anxious-roughneck-80", :DEPART 3, :DUREE 4, :PRIX 5}
              {:VOL "puzzled-wharf-81", :DEPART 3, :DUREE 9, :PRIX 15}
              {:VOL "quick-buddy-93", :DEPART 1, :DUREE 2, :PRIX 5}
              {:VOL "poor-ukulele-38", :DEPART 3, :DUREE 6, :PRIX 12}
              {:VOL "jealous-griddlecake-82", :DEPART 4, :DUREE 15, :PRIX 7})) => {:path ["quick-buddy-93" "puzzled-wharf-81"], :gain 20})

(fact
  (optimize [{:VOL "resonant-unit-62"            :DEPART 0   :DUREE 4 :PRIX 15}
             {:VOL "sparkling-ragweed-28"        :DEPART 1   :DUREE 2 :PRIX 1}
             {:VOL "nice-videodisc-57"           :DEPART 2   :DUREE 6 :PRIX 1}
             {:VOL "fancy-tambourine-7"          :DEPART 4   :DUREE 5 :PRIX 14}
             {:VOL "pleasant-sabotage-94"        :DEPART 5   :DUREE 2 :PRIX 18}
             {:VOL "thankful-veggie-90"          :DEPART 5   :DUREE 4 :PRIX 7}
             {:VOL "helpless-whaler-2"           :DEPART 6   :DUREE 2 :PRIX 2}
             {:VOL "crazy-collie-52"             :DEPART 7   :DUREE 6 :PRIX 3}
             {:VOL "obedient-bar-55"             :DEPART 9   :DUREE 5 :PRIX 15}
             {:VOL "frightened-strangeness-17"   :DEPART 10  :DUREE 2 :PRIX 4}
             {:VOL "squealing-moon-85"           :DEPART 10  :DUREE 4 :PRIX 8}
             {:VOL "squealing-tights-36"         :DEPART 11  :DUREE 2 :PRIX 7}
             {:VOL "melodic-bearer-87"           :DEPART 12  :DUREE 6 :PRIX 1}
             {:VOL "bad-dipstick-75"             :DEPART 14  :DUREE 5 :PRIX 11}
             {:VOL "gleaming-standby-50"         :DEPART 15  :DUREE 2 :PRIX 23}
             {:VOL "embarrassed-composer-15"     :DEPART 15  :DUREE 4 :PRIX 12}
             {:VOL "dark-transportation-18"      :DEPART 16  :DUREE 2 :PRIX 4}
             {:VOL "odd-smorgasbord-97"          :DEPART 17  :DUREE 6 :PRIX 2}
             {:VOL "tiny-wife-86"                :DEPART 19  :DUREE 5 :PRIX 20}
             {:VOL "gentle-slave-73"             :DEPART 20  :DUREE 2 :PRIX 3}
             {:VOL "careful-karaoke-87"          :DEPART 20  :DUREE 4 :PRIX 12}
             {:VOL "homeless-radiotherapy-20"    :DEPART 21  :DUREE 2 :PRIX 9}
             {:VOL "silly-sarcasm-56"            :DEPART 22  :DUREE 6 :PRIX 3}
             {:VOL "hollow-photography-8"        :DEPART 24  :DUREE 5 :PRIX 12}]) => {:gain 103
                                                                                      :path ["resonant-unit-62"
                                                                                             "pleasant-sabotage-94"
                                                                                             "obedient-bar-55"
                                                                                             "gleaming-standby-50"
                                                                                             "tiny-wife-86"
                                                                                             "hollow-photography-8"]})

(fact
  (optimize [{:VOL "resonant-unit-62"            :DEPART 0   :DUREE 4 :PRIX 15}
             {:VOL "sparkling-ragweed-28"        :DEPART 1   :DUREE 2 :PRIX 1}
             {:VOL "nice-videodisc-57"           :DEPART 2   :DUREE 6 :PRIX 1}
             {:VOL "fancy-tambourine-7"          :DEPART 4   :DUREE 5 :PRIX 14}
             {:VOL "pleasant-sabotage-94"        :DEPART 5   :DUREE 2 :PRIX 18}
             {:VOL "thankful-veggie-90"          :DEPART 5   :DUREE 4 :PRIX 7}
             {:VOL "helpless-whaler-2"           :DEPART 6   :DUREE 2 :PRIX 2}
             {:VOL "crazy-collie-52"             :DEPART 7   :DUREE 6 :PRIX 3}
             {:VOL "obedient-bar-55"             :DEPART 9   :DUREE 5 :PRIX 15}
             {:VOL "frightened-strangeness-17"   :DEPART 10  :DUREE 2 :PRIX 4}
             {:VOL "squealing-moon-85"           :DEPART 10  :DUREE 4 :PRIX 8}
             {:VOL "squealing-tights-36"         :DEPART 11  :DUREE 2 :PRIX 7}
             {:VOL "melodic-bearer-87"           :DEPART 12  :DUREE 6 :PRIX 1}
             {:VOL "bad-dipstick-75"             :DEPART 14  :DUREE 5 :PRIX 11}
             {:VOL "gleaming-standby-50"         :DEPART 15  :DUREE 2 :PRIX 23}
             {:VOL "embarrassed-composer-15"     :DEPART 15  :DUREE 4 :PRIX 12}
             {:VOL "dark-transportation-18"      :DEPART 16  :DUREE 2 :PRIX 4}
             {:VOL "odd-smorgasbord-97"          :DEPART 17  :DUREE 6 :PRIX 2}
             {:VOL "tiny-wife-86"                :DEPART 19  :DUREE 5 :PRIX 20}
             {:VOL "gentle-slave-73"             :DEPART 20  :DUREE 2 :PRIX 3}
             {:VOL "careful-karaoke-87"          :DEPART 20  :DUREE 4 :PRIX 12}
             {:VOL "homeless-radiotherapy-20"    :DEPART 21  :DUREE 2 :PRIX 9}
             {:VOL "silly-sarcasm-56"            :DEPART 22  :DUREE 6 :PRIX 3}
             {:VOL "hollow-photography-8"        :DEPART 24  :DUREE 5 :PRIX 12}
             {:VOL "combative-strand-71"         :DEPART 25  :DUREE 2 :PRIX 8}
             {:VOL "quiet-respirator-69"         :DEPART 25  :DUREE 4 :PRIX 15}
             {:VOL "combative-vat-43"            :DEPART 26  :DUREE 2 :PRIX 5}
             {:VOL "wild-leak-53"                :DEPART 27  :DUREE 6 :PRIX 7}
             {:VOL "anxious-yardage-91"          :DEPART 29  :DUREE 5 :PRIX 6}
             {:VOL "spotless-uniform-50"         :DEPART 30  :DUREE 2 :PRIX 24}
             {:VOL "fragile-visibility-28"       :DEPART 30  :DUREE 4 :PRIX 6}
             {:VOL "poor-memorandum-6"           :DEPART 31  :DUREE 2 :PRIX 3}
             {:VOL "disturbed-thunderstorm-99"   :DEPART 32  :DUREE 6 :PRIX 2}
             {:VOL "bored-patio-58"              :DEPART 34  :DUREE 5 :PRIX 12}
             {:VOL "expensive-petal-39"          :DEPART 35  :DUREE 2 :PRIX 27}
             {:VOL "depressed-sailing-47"        :DEPART 35  :DUREE 4 :PRIX 8}
             {:VOL "Early-word-50"               :DEPART 36  :DUREE 2 :PRIX 8}
             {:VOL "condemned-dollhouse-29"      :DEPART 37  :DUREE 6 :PRIX 6}
             {:VOL "careful-strictness-40"       :DEPART 39  :DUREE 5 :PRIX 10}
             {:VOL "eager-ump-31"                :DEPART 40  :DUREE 2 :PRIX 12}
             {:VOL "helpful-rye-48"              :DEPART 40  :DUREE 4 :PRIX 11}
             {:VOL "frantic-sack-1"              :DEPART 41  :DUREE 2 :PRIX 6}
             {:VOL "homeless-quirk-64"           :DEPART 42  :DUREE 6 :PRIX 6}
             {:VOL "proud-performer-72"          :DEPART 44  :DUREE 5 :PRIX 19}
             {:VOL "young-pawnbroker-17"         :DEPART 45  :DUREE 2 :PRIX 23}
             {:VOL "worried-crowd-87"            :DEPART 45  :DUREE 4 :PRIX 9}
             {:VOL "evil-handcuff-64"            :DEPART 46  :DUREE 2 :PRIX 2}
             {:VOL "delightful-jib-39"           :DEPART 47  :DUREE 6 :PRIX 3}
             {:VOL "precious-superman-68"        :DEPART 49  :DUREE 5 :PRIX 12}
             {:VOL "thoughtless-mascara-25"      :DEPART 50  :DUREE 2 :PRIX 30}
             {:VOL "determined-typhoid-97"       :DEPART 50  :DUREE 4 :PRIX 14}
             {:VOL "average-larynx-17"           :DEPART 51  :DUREE 2 :PRIX 2}
             {:VOL "cloudy-seascape-8"           :DEPART 52  :DUREE 6 :PRIX 3}
             {:VOL "charming-werewolf-53"        :DEPART 54  :DUREE 5 :PRIX 11}
             {:VOL "enthusiastic-chess-21"       :DEPART 55  :DUREE 2 :PRIX 9}
             {:VOL "noisy-narrator-1"            :DEPART 55  :DUREE 4 :PRIX 13}
             {:VOL "calm-tangelo-54"             :DEPART 56  :DUREE 2 :PRIX 4}
             {:VOL "swift-city-27"               :DEPART 57  :DUREE 6 :PRIX 6}
             {:VOL "filthy-lubricant-11"         :DEPART 59  :DUREE 5 :PRIX 16}
             {:VOL "zany-eve-38"                 :DEPART 60  :DUREE 2 :PRIX 19}
             {:VOL "huge-tape-25"                :DEPART 60  :DUREE 4 :PRIX 7}
             {:VOL "chubby-collarbone-64"        :DEPART 61  :DUREE 2 :PRIX 2}
             {:VOL "frightened-author-6"         :DEPART 62  :DUREE 6 :PRIX 2}
             {:VOL "beautiful-sleepwalker-49"    :DEPART 64  :DUREE 5 :PRIX 16}
             {:VOL "tired-mustache-19"           :DEPART 65  :DUREE 2 :PRIX 1}
             {:VOL "jealous-grapefruit-35"       :DEPART 65  :DUREE 4 :PRIX 15}
             {:VOL "confused-whisk-51"           :DEPART 66  :DUREE 2 :PRIX 3}
             {:VOL "big-underbelly-62"           :DEPART 67  :DUREE 6 :PRIX 5}
             {:VOL "real-ultrasound-35"          :DEPART 69  :DUREE 5 :PRIX 6}
             {:VOL "excited-theory-66"           :DEPART 70  :DUREE 2 :PRIX 10}
             {:VOL "fancy-ammunition-84"         :DEPART 70  :DUREE 4 :PRIX 9}
             {:VOL "big-coyote-53"               :DEPART 71  :DUREE 2 :PRIX 1}
             {:VOL "exuberant-beeswax-67"        :DEPART 72  :DUREE 6 :PRIX 7}
             {:VOL "resonant-loudmouth-53"       :DEPART 74  :DUREE 5 :PRIX 15}
             {:VOL "small-sweettalk-96"          :DEPART 75  :DUREE 2 :PRIX 6}
             {:VOL "gentle-speedometer-75"       :DEPART 75  :DUREE 4 :PRIX 8}
             {:VOL "ugly-jackhammer-17"          :DEPART 76  :DUREE 2 :PRIX 6}
             {:VOL "screeching-tailspin-14"      :DEPART 77  :DUREE 6 :PRIX 3}
             {:VOL "dark-bathtub-58"             :DEPART 79  :DUREE 5 :PRIX 8}
             {:VOL "ugly-telegraph-87"           :DEPART 80  :DUREE 2 :PRIX 7}
             {:VOL "tough-interpreter-23"        :DEPART 80  :DUREE 4 :PRIX 11}
             {:VOL "frantic-cornflakes-56"       :DEPART 81  :DUREE 2 :PRIX 9}
             {:VOL "jolly-memorandum-21"         :DEPART 82  :DUREE 6 :PRIX 4}
             {:VOL "ill-pedestal-66"             :DEPART 84  :DUREE 5 :PRIX 20}
             {:VOL "square-medalist-41"          :DEPART 85  :DUREE 2 :PRIX 21}
             {:VOL "cloudy-pail-90"              :DEPART 85  :DUREE 4 :PRIX 12}
             {:VOL "crazy-wisdom-17"             :DEPART 86  :DUREE 2 :PRIX 1}
             {:VOL "courageous-actress-5"        :DEPART 87  :DUREE 6 :PRIX 6}
             {:VOL "lovely-pacemaker-23"         :DEPART 89  :DUREE 5 :PRIX 15}
             {:VOL "ill-burger-66"               :DEPART 90  :DUREE 2 :PRIX 1}
             {:VOL "open-tourist-23"             :DEPART 90  :DUREE 4 :PRIX 15}
             {:VOL "mute-upstairs-87"            :DEPART 91  :DUREE 2 :PRIX 8}
             {:VOL "impossible-ax-43"            :DEPART 92  :DUREE 6 :PRIX 4}
             {:VOL "poised-starch-83"            :DEPART 94  :DUREE 5 :PRIX 5}
             {:VOL "chubby-burger-30"            :DEPART 95  :DUREE 2 :PRIX 17}
             {:VOL "short-throwback-58"          :DEPART 95  :DUREE 4 :PRIX 15}
             {:VOL "brave-numerous-37"           :DEPART 96  :DUREE 2 :PRIX 3}
             {:VOL "nasty-tool-16"               :DEPART 97  :DUREE 6 :PRIX 6}
             {:VOL "crowded-sextet-79"           :DEPART 99  :DUREE 5 :PRIX 9}
             {:VOL "gleaming-upstart-94"         :DEPART 100 :DUREE 2 :PRIX 8}
             {:VOL "naughty-connoisseur-12"      :DEPART 100 :DUREE 4 :PRIX 13}
             {:VOL "late-bikini-58"              :DEPART 101 :DUREE 2 :PRIX 10}
             {:VOL "busy-sportsman-40"           :DEPART 102 :DUREE 6 :PRIX 5}
             {:VOL "tiny-farmyard-55"            :DEPART 104 :DUREE 5 :PRIX 11}
             {:VOL "motionless-wrinkle-34"       :DEPART 105 :DUREE 2 :PRIX 16}
             {:VOL "talented-scrubber-60"        :DEPART 105 :DUREE 4 :PRIX 13}
             {:VOL "tense-somewhat-56"           :DEPART 106 :DUREE 2 :PRIX 9}
             {:VOL "important-vandal-62"         :DEPART 107 :DUREE 6 :PRIX 6}
             {:VOL "chubby-punch-28"             :DEPART 109 :DUREE 5 :PRIX 17}
             {:VOL "naughty-footwear-44"         :DEPART 110 :DUREE 2 :PRIX 5}
             {:VOL "big-tollbooth-78"            :DEPART 110 :DUREE 4 :PRIX 13}
             {:VOL "chubby-rehab-38"             :DEPART 111 :DUREE 2 :PRIX 8}
             {:VOL "anxious-supertanker-38"      :DEPART 112 :DUREE 6 :PRIX 4}
             {:VOL "shallow-puzzle-4"            :DEPART 114 :DUREE 5 :PRIX 23}
             {:VOL "mushy-telecommunications-43" :DEPART 115 :DUREE 2 :PRIX 2}
             {:VOL "blushing-arc-59"             :DEPART 115 :DUREE 4 :PRIX 7}
             {:VOL "nervous-union-74"            :DEPART 116 :DUREE 2 :PRIX 6}
             {:VOL "drab-peg-77"                 :DEPART 117 :DUREE 6 :PRIX 6}
             {:VOL "super-quarterback-71"        :DEPART 119 :DUREE 5 :PRIX 15}
             {:VOL "stupid-babbler-56"           :DEPART 120 :DUREE 2 :PRIX 22}
             {:VOL "precious-poppycock-9"        :DEPART 120 :DUREE 4 :PRIX 6}
             {:VOL "helpless-badminton-82"       :DEPART 121 :DUREE 2 :PRIX 1}
             {:VOL "arrogant-comic-22"           :DEPART 122 :DUREE 6 :PRIX 4}
             {:VOL "inquisitive-dessert-89"      :DEPART 124 :DUREE 5 :PRIX 11}
             {:VOL "raspy-comic-93"              :DEPART 125 :DUREE 2 :PRIX 3}
             {:VOL "great-crane-86"              :DEPART 125 :DUREE 4 :PRIX 6}
             {:VOL "jittery-rainstorm-42"        :DEPART 126 :DUREE 2 :PRIX 3}
             {:VOL "important-mushroom-51"       :DEPART 127 :DUREE 6 :PRIX 7}
             {:VOL "scrawny-muzzle-77"           :DEPART 129 :DUREE 5 :PRIX 17}
             {:VOL "brave-snag-51"               :DEPART 130 :DUREE 2 :PRIX 6}
             {:VOL "prickly-plantation-71"       :DEPART 130 :DUREE 4 :PRIX 10}
             {:VOL "elegant-butcher-41"          :DEPART 131 :DUREE 2 :PRIX 5}
             {:VOL "quiet-warlord-58"            :DEPART 132 :DUREE 6 :PRIX 6}
             {:VOL "aggressive-ballerina-21"     :DEPART 134 :DUREE 5 :PRIX 17}
             {:VOL "clear-cervix-77"             :DEPART 135 :DUREE 2 :PRIX 10}
             {:VOL "angry-waterfall-38"          :DEPART 135 :DUREE 4 :PRIX 15}
             {:VOL "large-metropolis-69"         :DEPART 136 :DUREE 2 :PRIX 10}
             {:VOL "creepy-omelette-18"          :DEPART 137 :DUREE 6 :PRIX 1}
             {:VOL "comfortable-landlady-44"     :DEPART 139 :DUREE 5 :PRIX 11}
             {:VOL "quaint-jacuzzi-98"           :DEPART 140 :DUREE 2 :PRIX 4}
             {:VOL "sparkling-stove-10"          :DEPART 140 :DUREE 4 :PRIX 12}
             {:VOL "plain-train-87"              :DEPART 141 :DUREE 2 :PRIX 1}
             {:VOL "splendid-statue-47"          :DEPART 142 :DUREE 6 :PRIX 6}
             {:VOL "nervous-tundra-10"           :DEPART 144 :DUREE 5 :PRIX 13}
             {:VOL "vast-book-77"                :DEPART 145 :DUREE 2 :PRIX 1}
             {:VOL "poor-drunk-56"               :DEPART 145 :DUREE 4 :PRIX 15}
             {:VOL "worried-material-68"         :DEPART 146 :DUREE 2 :PRIX 4}
             {:VOL "hushed-poppycock-32"         :DEPART 147 :DUREE 6 :PRIX 2}
             {:VOL "confused-fire-90"            :DEPART 149 :DUREE 5 :PRIX 6}
             {:VOL "smiling-gut-68"              :DEPART 150 :DUREE 2 :PRIX 9}
             {:VOL "clumsy-radiologist-58"       :DEPART 150 :DUREE 4 :PRIX 7}
             {:VOL "blue-eyed-stain-87"          :DEPART 151 :DUREE 2 :PRIX 5}
             {:VOL "thoughtful-swan-32"          :DEPART 152 :DUREE 6 :PRIX 7}
             {:VOL "screeching-condo-4"          :DEPART 154 :DUREE 5 :PRIX 20}
             {:VOL "odd-viper-39"                :DEPART 155 :DUREE 2 :PRIX 18}
             {:VOL "obnoxious-jeep-48"           :DEPART 155 :DUREE 4 :PRIX 10}
             {:VOL "homely-geek-48"              :DEPART 156 :DUREE 2 :PRIX 9}
             {:VOL "spotless-nozzle-91"          :DEPART 157 :DUREE 6 :PRIX 7}
             {:VOL "helpless-insect-12"          :DEPART 159 :DUREE 5 :PRIX 8}
             {:VOL "annoying-seashell-48"        :DEPART 160 :DUREE 2 :PRIX 18}
             {:VOL "thoughtless-windmill-9"      :DEPART 160 :DUREE 4 :PRIX 8}
             {:VOL "terrible-thesis-95"          :DEPART 161 :DUREE 2 :PRIX 6}
             {:VOL "vast-paunch-6"               :DEPART 162 :DUREE 6 :PRIX 4}
             {:VOL "powerful-jam-46"             :DEPART 164 :DUREE 5 :PRIX 5}
             {:VOL "troubled-backpacker-38"      :DEPART 165 :DUREE 2 :PRIX 5}
             {:VOL "late-ring-4"                 :DEPART 165 :DUREE 4 :PRIX 15}
             {:VOL "relieved-wire-43"            :DEPART 166 :DUREE 2 :PRIX 10}
             {:VOL "jolly-cheetah-26"            :DEPART 167 :DUREE 6 :PRIX 3}
             {:VOL "outrageous-quid-75"          :DEPART 169 :DUREE 5 :PRIX 13}
             {:VOL "ancient-accelerator-21"      :DEPART 170 :DUREE 2 :PRIX 17}
             {:VOL "enchanting-pilnnnlowcase-66" :DEPART 170 :DUREE 4 :PRIX 9}
             {:VOL "itchy-karate-2"              :DEPART 171 :DUREE 2 :PRIX 2}
             {:VOL "hilarious-cutoffs-38"        :DEPART 172 :DUREE 6 :PRIX 1}
             {:VOL "inquisitive-metro-31"        :DEPART 174 :DUREE 5 :PRIX 17}
             {:VOL "crowded-nutcracker-79"       :DEPART 175 :DUREE 2 :PRIX 1}
             {:VOL "great-malaria-71"            :DEPART 175 :DUREE 4 :PRIX 11}
             {:VOL "puny-cheese-88"              :DEPART 176 :DUREE 2 :PRIX 6}
             {:VOL "powerful-campfire-52"        :DEPART 177 :DUREE 6 :PRIX 6}
             {:VOL "happy-oaf-48"                :DEPART 179 :DUREE 5 :PRIX 11}
             {:VOL "innocent-crown-60"           :DEPART 180 :DUREE 2 :PRIX 3}
             {:VOL "angry-rodent-98"             :DEPART 180 :DUREE 4 :PRIX 13}
             {:VOL "jolly-labyrinth-19"          :DEPART 181 :DUREE 2 :PRIX 2}
             {:VOL "filthy-science-35"           :DEPART 182 :DUREE 6 :PRIX 7}
             {:VOL "condemned-sequel-76"         :DEPART 184 :DUREE 5 :PRIX 11}
             {:VOL "zealous-louse-40"            :DEPART 185 :DUREE 2 :PRIX 8}
             {:VOL "melodic-rainfall-95"         :DEPART 185 :DUREE 4 :PRIX 9}
             {:VOL "annoying-lip-77"             :DEPART 186 :DUREE 2 :PRIX 8}
             {:VOL "harsh-explosion-21"          :DEPART 187 :DUREE 6 :PRIX 2}
             {:VOL "resonant-chip-72"            :DEPART 189 :DUREE 5 :PRIX 17}
             {:VOL "open-sparrow-92"             :DEPART 190 :DUREE 2 :PRIX 28}
             {:VOL "good-numismatics-90"         :DEPART 190 :DUREE 4 :PRIX 15}
             {:VOL "long-writeoff-63"            :DEPART 191 :DUREE 2 :PRIX 4}
             {:VOL "clever-player-74"            :DEPART 192 :DUREE 6 :PRIX 1}
             {:VOL "clumsy-congress-66"          :DEPART 194 :DUREE 5 :PRIX 13}
             {:VOL "helpful-wheat-76"            :DEPART 195 :DUREE 2 :PRIX 3}
             {:VOL "wrong-lace-67"               :DEPART 195 :DUREE 4 :PRIX 12}
             {:VOL "joyous-hemp-89"              :DEPART 196 :DUREE 2 :PRIX 7}
             {:VOL "mammoth-vat-92"              :DEPART 197 :DUREE 6 :PRIX 3}
             {:VOL "wandering-pasteboard-75"     :DEPART 199 :DUREE 5 :PRIX 13}
             {:VOL "flat-blob-65"                :DEPART 200 :DUREE 2 :PRIX 15}
             {:VOL "alive-schoolmarm-41"         :DEPART 200 :DUREE 4 :PRIX 12}
             {:VOL "adorable-kidnapper-99"       :DEPART 201 :DUREE 2 :PRIX 7}
             {:VOL "raspy-slumlord-81"           :DEPART 202 :DUREE 6 :PRIX 4}
             {:VOL "kind-knapsack-80"            :DEPART 204 :DUREE 5 :PRIX 22}
             {:VOL "helpless-vendor-89"          :DEPART 205 :DUREE 2 :PRIX 21}
             {:VOL "poor-podiatry-56"            :DEPART 205 :DUREE 4 :PRIX 8}
             {:VOL "old-fashioned-slug-31"       :DEPART 206 :DUREE 2 :PRIX 7}
             {:VOL "cheerful-keystroke-76"       :DEPART 207 :DUREE 6 :PRIX 2}
             {:VOL "hollow-shoreline-39"         :DEPART 209 :DUREE 5 :PRIX 8}
             {:VOL "repulsive-cook-69"           :DEPART 210 :DUREE 2 :PRIX 30}
             {:VOL "wandering-stepladder-94"     :DEPART 210 :DUREE 4 :PRIX 12}
             {:VOL "comfortable-penny-3"         :DEPART 211 :DUREE 2 :PRIX 4}
             {:VOL "flipped-out-thanksgiving-74" :DEPART 212 :DUREE 6 :PRIX 4}
             {:VOL "scrawny-boat-42"             :DEPART 214 :DUREE 5 :PRIX 22}
             {:VOL "anxious-canoeist-29"         :DEPART 215 :DUREE 2 :PRIX 11}
             {:VOL "cruel-leg-44"                :DEPART 215 :DUREE 4 :PRIX 13}
             {:VOL "great-kneecap-18"            :DEPART 216 :DUREE 2 :PRIX 1}
             {:VOL "healthy-theoretician-3"      :DEPART 217 :DUREE 6 :PRIX 5}
             {:VOL "vivacious-peace-9"           :DEPART 219 :DUREE 5 :PRIX 13}
             {:VOL "lively-eggshell-46"          :DEPART 220 :DUREE 2 :PRIX 6}
             {:VOL "combative-rabies-27"         :DEPART 220 :DUREE 4 :PRIX 13}
             {:VOL "unusual-bargain-30"          :DEPART 221 :DUREE 2 :PRIX 8}
             {:VOL "fantastic-tractor-16"        :DEPART 222 :DUREE 6 :PRIX 4}
             {:VOL "quaint-magnesium-29"         :DEPART 224 :DUREE 5 :PRIX 15}
             {:VOL "delightful-lightning-45"     :DEPART 225 :DUREE 2 :PRIX 24}
             {:VOL "uninterested-hog-74"         :DEPART 225 :DUREE 4 :PRIX 7}
             {:VOL "excited-hive-8"              :DEPART 226 :DUREE 2 :PRIX 7}
             {:VOL "unsightly-balcony-44"        :DEPART 227 :DUREE 6 :PRIX 5}
             {:VOL "splendid-plan-32"            :DEPART 229 :DUREE 5 :PRIX 16}
             {:VOL "uptight-acid-27"             :DEPART 230 :DUREE 2 :PRIX 6}
             {:VOL "homely-saucepan-52"          :DEPART 230 :DUREE 4 :PRIX 14}
             {:VOL "colorful-padlock-35"         :DEPART 231 :DUREE 2 :PRIX 10}
             {:VOL "helpless-iguana-54"          :DEPART 232 :DUREE 6 :PRIX 5}
             {:VOL "plain-trench-81"             :DEPART 234 :DUREE 5 :PRIX 9}
             {:VOL "hushed-camouflage-5"         :DEPART 235 :DUREE 2 :PRIX 16}
             {:VOL "drab-ink-48"                 :DEPART 235 :DUREE 4 :PRIX 13}
             {:VOL "expensive-panacea-3"         :DEPART 236 :DUREE 2 :PRIX 5}
             {:VOL "disturbed-worker-40"         :DEPART 237 :DUREE 6 :PRIX 3}
             {:VOL "busy-snail-31"               :DEPART 239 :DUREE 5 :PRIX 16}
             {:VOL "successful-squalor-64"       :DEPART 240 :DUREE 2 :PRIX 20}
             {:VOL "excited-performer-4"         :DEPART 240 :DUREE 4 :PRIX 8}
             {:VOL "gigantic-bullfighter-75"     :DEPART 241 :DUREE 2 :PRIX 7}
             {:VOL "great-raft-53"               :DEPART 242 :DUREE 6 :PRIX 3}
             {:VOL "successful-genie-28"         :DEPART 244 :DUREE 5 :PRIX 15}
             {:VOL "short-curve-96"              :DEPART 245 :DUREE 2 :PRIX 29}
             {:VOL "light-stereo-78"             :DEPART 245 :DUREE 4 :PRIX 15}
             {:VOL "voiceless-sophomore-70"      :DEPART 246 :DUREE 2 :PRIX 2}
             {:VOL "victorious-syrup-33"         :DEPART 247 :DUREE 6 :PRIX 2}
             {:VOL "powerful-motorist-28"        :DEPART 249 :DUREE 5 :PRIX 18}
             {:VOL "confused-sterilization-85"   :DEPART 250 :DUREE 2 :PRIX 28}]) => {:gain 918, :path ["resonant-unit-62" "pleasant-sabotage-94" "obedient-bar-55" "gleaming-standby-50" "tiny-wife-86" "quiet-respirator-69" "spotless-uniform-50" "expensive-petal-39" "eager-ump-31" "young-pawnbroker-17" "thoughtless-mascara-25" "noisy-narrator-1" "zany-eve-38" "beautiful-sleepwalker-49" "excited-theory-66" "resonant-loudmouth-53" "tough-interpreter-23" "square-medalist-41" "open-tourist-23" "chubby-burger-30" "naughty-connoisseur-12" "motionless-wrinkle-34" "chubby-punch-28" "shallow-puzzle-4" "stupid-babbler-56" "inquisitive-dessert-89" "scrawny-muzzle-77" "aggressive-ballerina-21" "sparkling-stove-10" "poor-drunk-56" "smiling-gut-68" "screeching-condo-4" "annoying-seashell-48" "late-ring-4" "ancient-accelerator-21" "inquisitive-metro-31" "angry-rodent-98" "condemned-sequel-76" "open-sparrow-92" "clumsy-congress-66" "flat-blob-65" "kind-knapsack-80" "repulsive-cook-69" "scrawny-boat-42" "combative-rabies-27" "delightful-lightning-45" "splendid-plan-32" "hushed-camouflage-5" "successful-squalor-64" "short-curve-96" "confused-sterilization-85"]})
