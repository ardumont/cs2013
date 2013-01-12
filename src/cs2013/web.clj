(ns cs2013.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site api]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :as trace]
            [ring.middleware.session :as session]
            [ring.middleware.session.cookie :as cookie]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.basic-authentication :as basic]
            [cemerick.drawbridge :as drawbridge]
            [environ.core :refer [env]]
            [clojure.tools.trace :only [trace] :as t]
            [cs2013.enonce1 :as enonce1]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [cs2013.mail :as mail]
            [cs2013.response :as r]
            [cs2013.operations :as o]
            [cs2013.middleware :as w]))

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def ^:private drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Answering queries 'q='
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def deal-with-query nil);; small trick when using demulti
(defmulti deal-with-query identity)

(defmethod deal-with-query "Quelle est ton adresse email" [_] (r/body-response (mail/my)))
(defmethod deal-with-query "Es tu abonne a la mailing list(OUI/NON)" [_] (r/body-response "OUI"))
(defmethod deal-with-query "Es tu heureux de participer(OUI/NON)" [_] (r/body-response "OUI"))
(defmethod deal-with-query "Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)" [_] (r/body-response "OUI"))
(defmethod deal-with-query "Est ce que tu reponds toujours oui(OUI/NON)" [_] (r/body-response "NON"))
(defmethod deal-with-query "As tu bien recu le premier enonce(OUI/NON)" [_] (r/body-response "OUI"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; registering bodies problems inside atom
;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; not perfect because we lost the registered request as each deployment but better than nothing at the moment
(def ^{:doc "post bodies registered"}
  bodies (atom {}))

;; a route to expose the problems registered
(defmethod deal-with-query "enonces" [_] (r/body-response (pr-str @bodies)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; query operations
;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; deal with operation query
(def deal-with-operation nil)

;; dispatch on the presence of the \(
(defmulti deal-with-operation (fn [q] (some #{\(} q)))

;; naive operations
(defmethod deal-with-operation nil [q] (o/compute-simple-operation q))

;; those with \( \)
(defmethod deal-with-operation \(  [q] (-> q o/compute-operation float))

(defmethod deal-with-query :default [q] (-> q deal-with-operation str r/body-response))

(defn- deal-with-body
  "One function to deal with body/original-body (trace, register in atom, anything)"
  [{:keys [body encoding]} key]
  (let [b (slurp body :encoding encoding)]
    (t/trace "body: " b)
    (swap! bodies #(update-in % [key] (fn [_] b)))
    b))

;; the main routing
(defroutes app
  ;; play with remote repl
  (ANY "/repl" {:as req}
       (drawbridge req))

  ;; deal with questions
  (GET "/" [q]
       (deal-with-query q))

  ;; reception of the problem
  (POST "/enonce/1" {:as req}
        (-> req (deal-with-body :enonce-1) r/post-body-response))

  ;; first problem
  (GET "/scalaskel/change/:n" [n]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (-> n read-string enonce1/decomp json/write-str)})

  ;; everything else
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(def default-port 5000)
(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) default-port))
        ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
        store (cookie/cookie-store {:key (env :session-secret)})]
    (jetty/run-jetty (-> #'app
                         ((if (env :production)
                            w/wrap-error-page
                            trace/wrap-stacktrace))
                         (site {:session {:store store}})
                         w/wrap-request-logging
                         w/wrap-correct-content-type)
                     {:port port :join? false})))

(comment ;; interactive dev
  (do
    (.stop jetty-server)
    (def jetty-server (-main))))
