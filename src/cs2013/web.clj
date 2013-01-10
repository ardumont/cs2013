(ns cs2013.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :as trace]
            [ring.middleware.session :as session]
            [ring.middleware.session.cookie :as cookie]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.basic-authentication :as basic]
            [cemerick.drawbridge :as drawbridge]
            [environ.core :refer [env]]
            [clojure.tools.trace :only [trace deftrace trace-forms trace-ns untrace-ns trace-vars] :as t]))

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def ^:private drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(defn- my-mail
  "Just my email so that robots do not find it"
  []
  (format "%s.%s@%s.%s" "eniotna" "t" "gmail" "com"))

(defn body-response
  [m]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body m})

(def deal-with-query nil)
(defmulti deal-with-query identity)

(defmethod deal-with-query "Quelle est ton adresse email" [_] (body-response (my-mail)))
(defmethod deal-with-query "Es tu abonne a la mailing list(OUI/NON)" [_] (body-response "OUI"))
(defmethod deal-with-query "Es tu heureux de participer(OUI/NON)" [_] (body-response "OUI"))
(defmethod deal-with-query "Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)" [_] (body-response "OUI"))
(defmethod deal-with-query "Est ce que tu reponds toujours oui(OUI/NON)" [_] (body-response "NON"))
(defmethod deal-with-query "As tu bien recu le premier enonce(OUI/NON)" [_] (body-response "NON"))

(defroutes app
  (ANY "/repl" {:as req}
       (drawbridge req))
  (GET "/" [q]
       (deal-with-query q))
  (POST "/enonce/1" {body :body}
        (let [b (slurp body)]
          (t/trace "body: " b)
          (body-response b)))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn- log [msg & vals]
  (let [line (apply format msg vals)]
    (t/trace line)))

(defn wrap-request-logging [handler]
  (fn [{:keys [request-method uri body] :as req}]
    (t/trace "request processed: " req)
    (let [resp (handler req)]
      (log "Processing %s %s" request-method uri)
      resp)))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(def default-port 5000)
(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) default-port))
        ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
        store (cookie/cookie-store {:key (env :session-secret)})]
    (jetty/run-jetty (-> #'app
                         wrap-request-logging
                         ((if (env :production)
                            wrap-error-page
                            trace/wrap-stacktrace))
                         (site {:session {:store store}}))
                     {:port port :join? false})))

(comment ;; For interactive development:
  (def jetty-server (-main))
  (.stop jetty-server))
