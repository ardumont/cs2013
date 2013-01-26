(ns ^{:doc "The second problem received via post request + solutions"}
  cs2013.enonce2
  (:require [clojure.tools.trace :only [trace] :as t]))

;; ## Location d'astronef sur Jajascript

;; Votre cousin par alliance, Martin O. sur la planète Jajascript vient de monter sa petite entreprise de vol spatial privée: Jajascript Flight Rental. Il loue aux grosses corporations son astronef lorsqu'elles ont de fortes charges ou un pépin avec leurs propres appareils. Il s'occupe de la maintenance et de l'entretien de son petit astronef. Il ne pouvait s'en payer qu'un pour démarrer.

;; Ces grosses corporations envoient des commandes de location qui consistent en un intervalle de temps, et le prix qu'ils sont prêts à payer pour louer l'astronef durant cet intervalle.

;; Les commandes de tous les clients sont connues plusieurs jours à l'avance. Ce qui permet de faire un planning pour une journée.
;; Les commandes viennent de plusieurs sociétés différentes et parfois elles se chevauchent. On ne peut donc pas toutes les honorer.

;; Idéalement, il faut donc être capable de prendre les plus rentables, histoire de maximiser les gains de sa petite entreprise, et de s'acheter d'autres astronefs.
;; Votre cousin passe des heures à trouver le planning idéal et vous demande **pour un planning donné de calculer une solution qui maximise son gain**.

;; ### Exemple

;; Considérez par exemple le cas où la JajaScript Flight Rental a 4 commandes :

;;      MONAD42 : heure de départ 0, durée 5, prix 10
;;      META18 : heure de départ 3, durée 7, prix 14
;;      LEGACY01 : heure de départ 5, durée 9, prix 8
;;      YAGNI17 : heure de départ 5, durée 9, prix 7

;; La solution optimale consiste à accepter MONAD42 et LEGACY01, et le revenu est de `10 + 8 = 18`. Remarquez qu'une solution à partir de MONAD42 et YAGNI17 est faisable (l'avion serait loué sans interruption de 0 à 14) mais non optimale car le bénéfice serait que de 17.

;; ### Précisions

;; L'identifiant d'un vol ne dépasse jamais 50 caractères,
;; les heures de départs, durée et prix sont des entiers positifs raisonnablement grands.

;; ### Serveur

;; Votre serveur doit répondre aux requêtes http POST de la forme `http://serveur/jajascript/optimize` avec un payload de la forme :

;;      [
;;              { "VOL": "NOM_VOL", "DEPART": HEURE, "DUREE": DUREE, "PRIX": PRIX },
;;              ...
;;      ]

;; En reprenant l'exemple ci dessus :

;;      [
;;              { "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 },
;;              { "VOL": "META18", "DEPART": 3, "DUREE": 7, "PRIX": 14 },
;;              { "VOL": "LEGACY01", "DEPART": 5, "DUREE": 9, "PRIX": 8 },
;;              { "VOL": "YAGNI17", "DEPART": 5, "DUREE": 9, "PRIX": 7 }
;;      ]

;; Vous devrez répondre le résultat suivant :

;;      {
;;              "gain" : 18,
;;              "path" : ["MONAD42","LEGACY01"]
;;      }

;; Le gain représentant la somme optimale, path représentant l'ordre des vols.

;; Bons calculs !

;; Input from web.clj
;; [{"DUREE" 5, "PRIX" 10, "VOL" "MONAD42", "DEPART" 0}
;;  {"DUREE" 7, "PRIX" 14, "VOL" "META18", "DEPART" 3}
;;  {"DUREE" 9, "PRIX" 8, "VOL" "LEGACY01", "DEPART" 5}
;;  {"DUREE" 9, "PRIX" 7, "VOL" "YAGNI17", "DEPART" 5}]

(defn adjacents
  "Compute the nodes data (group by identity, the unique id VOL)"
  [vm]
  (reduce
   (fn [adjs {:keys [DEPART DUREE] :as node}]
     (assoc adjs node (filter (comp (partial <= (+ DEPART DUREE)) :DEPART) vm)))
   {}
   vm))

(defn build-tree
  "breadth-first lazily building the tree"
  [adjs root-node]
  ((fn next-elt [queue]
     (lazy-seq
      (when (seq queue)
        (let [{:keys [id gain path] :as node} (peek queue)
              children  (->> node
                             :id
                             adjs
                             (map (fn [{:keys [VOL PRIX] :as node}]
                                    {:id node
                                     :gain (+ gain PRIX)
                                     :path (conj path VOL)})))]
          (cons node (->> children
                          (into (pop queue))
                          next-elt))))))
   (conj clojure.lang.PersistentQueue/EMPTY root-node)))

(defn build-tree-root
  "Begin the building of the tree"
  [adjs {:keys [VOL PRIX] :as node}]
  (build-tree adjs {:id node :gain PRIX :path [VOL]}))

(defn best-path
  "Compute the best paths from a list of path"
  [gain-paths]
  (-> (apply max-key :gain gain-paths)
      (select-keys [:gain :path])))

(defn optimize
  "Entry point for the second problem"
  [vm]
  (let [adjs (adjacents vm)]
    (->> vm
         (mapcat (partial build-tree-root adjs))
         best-path)))
