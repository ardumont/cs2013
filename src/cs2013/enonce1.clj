(ns ^{:doc "The first problem received via post request + solutions"}
    cs2013.enonce1
  (:use [midje.sweet]))

;; L'echoppe de monade sur Scalaskel.

;; Sur la planete Scalaskel, une planete en marge de la galaxie, aux confins de l'univers, la monnaie se compte en cents, comme chez nous.
;; 100 cents font un groDessimal. Le groDessimal est la monnaie standard utilisable partout sur toutes les planetes de l'univers connu.
;; C'est un peu complique a manipuler, mais si on ne s'en sert pas y'a toujours des erreurs d'arrondis incroyables quand on les soustrais ou on les divise,
;; c'est idiot, mais c'est comme ca.

;; Sur Scalaskel, on utilise rarement des groDessimaux, on utilise des pieces plus petites :
;; Le **Foo** vaut **1 cent**,
;; le **Bar** vaut **7 cents**,
;; le **Qix** vaut **11 cents**
;; et le **Baz** vaut **21 cents**.

;; Vous tenez une echoppe de monade et autres variables meta-syntaxique sur Scalaskel.
;; Pour faire face a l'afflux de touristes etrangers avec les poches remplies de groDessimaux, vous avez besoin d'ecrire un programme qui pour toute somme de 1 a 100
;; cents, vous donnera toutes les decompositions possibles en pieces de **Foo**, **Bar**, **Qix** ou **Baz**.

;; Par exemple, 1 cent ne peut se decomposer qu'en une seule piece Foo.
;; Par contre 7 cents peuvent se decomposer soit en 7 pieces Foo, soit en 1 piece Bar.

;; Serveur Web :
;; Votre serveur doit repondre aux requetes http GET de la forme `http://serveur/scalaskel/change/X`, `X` etant une valeur en cents de 1 a 100 cents.
;; La reponse attendue est un json de la forme :
;;     [{"foo": w, "bar", x, "qix", y, "baz": z}, ?]

;;     Exemples:
;; Pour `http://serveur/scalaskel/change/1` il faut repondre :
;;     [{"foo": 1}]

;; Pour `http://serveur/scalaskel/change/7` il faut repondre :
;;     [{"foo": 7}, {"bar": 1}]

;; L'ordre des valeurs dans le tableau json, ainsi que le formatage n'a pas d'importance a partir du moment ou c'est du json valide, il s'entends.
;; Bon courage !

(def cur {:foo           1
          :bar           7
          :qix           11
          :baz           21})

(defn decomp
  "Compute the decomposition of foo/bar/qix/baz"
  [n]
  (into []
        (let [r (range 0 (inc n))]
          (for [w r
                x r
                y r
                z r
                :when (= n (+ (* 1 w) (* 7 x) (* 11 y) (* 21 z)))]
            {:foo w
             :bar x
             :qix y
             :baz z}))))

(fact "Trying out the first 10 numbers with decomp"
 (map decomp (range 0 10)) => '([{:foo 0, :bar 0, :qix 0, :baz 0}]
                                [{:foo 1, :bar 0, :qix 0, :baz 0}]
                                [{:foo 2, :bar 0, :qix 0, :baz 0}]
                                [{:foo 3, :bar 0, :qix 0, :baz 0}]
                                [{:foo 4, :bar 0, :qix 0, :baz 0}]
                                [{:foo 5, :bar 0, :qix 0, :baz 0}]
                                [{:foo 6, :bar 0, :qix 0, :baz 0}]
                                [{:foo 0, :bar 1, :qix 0, :baz 0}
                                 {:foo 7, :bar 0, :qix 0, :baz 0}]
                                [{:foo 1, :bar 1, :qix 0, :baz 0}
                                 {:foo 8, :bar 0, :qix 0, :baz 0}]
                                [{:foo 2, :bar 1, :qix 0, :baz 0}
                                 {:foo 9, :bar 0, :qix 0, :baz 0}]))
