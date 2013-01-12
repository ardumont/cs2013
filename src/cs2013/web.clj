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
            [clojure.string :as str]))

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def ^:private drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(defn- my-mail
  "My email"
  []
  (format "%s.%s@%s.%s" "eniotna" "t" "gmail" "com"))

(defn- body-response
  "Answering request"
  [message]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body message})

(def deal-with-query nil);; small trick when using demulti
(defmulti deal-with-query identity)

(defmethod deal-with-query "Quelle est ton adresse email" [_] (body-response (my-mail)))
(defmethod deal-with-query "Es tu abonne a la mailing list(OUI/NON)" [_] (body-response "OUI"))
(defmethod deal-with-query "Es tu heureux de participer(OUI/NON)" [_] (body-response "OUI"))
(defmethod deal-with-query "Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)" [_] (body-response "OUI"))
(defmethod deal-with-query "Est ce que tu reponds toujours oui(OUI/NON)" [_] (body-response "NON"))
(defmethod deal-with-query "As tu bien recu le premier enonce(OUI/NON)" [_] (body-response "OUI"))

;; not perfect because we lost the registered request as each deployment but better than nothing at the moment
(def ^{:doc "post bodies registered"}
  bodies (atom {}))

(defmethod deal-with-query "enonces" [_] (body-response (pr-str @bodies)))

(defn char2int
  [c]
  (- (int c) (int \0)))

(defmethod deal-with-query :default
  [q]
  (let [m {\space +
           \* *
           \/ /
           \- -}
        [a op b] q
        x (char2int a)
        y (char2int b)]
    (-> ((m op) x y) str body-response)))


(defn- post-body-response
  "Answering request"
  [m]
  {:status 201
   :headers {"Content-Type" "text/plain"}
   :body m})

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
  ;; deal with naive questions
  (GET "/" [q]
       (deal-with-query q))
  (GET "/scalaskel/change/:n" [n]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (-> n read-string enonce1/decomp json/write-str)})
  ;; reception of the problem
  (POST "/enonce/1" {:as req}
        (-> req (deal-with-body :enonce-1) post-body-response))
  ;; everything else
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn wrap-request-logging
  "Log request middleware"
  [handler]
  (fn [req]
    (t/trace req)
    (handler req)))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(defn wrap-correct-content-type [handler]
  (fn [req]
    (if (= "application/x-www-form-urlencoded" (:content-type req))
      (handler (assoc req :content-type "application/json"))
      (handler req))))

(def default-port 5000)
(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) default-port))
        ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
        store (cookie/cookie-store {:key (env :session-secret)})]
    (jetty/run-jetty (-> #'app
                         ((if (env :production)
                            wrap-error-page
                            trace/wrap-stacktrace))
                         (site {:session {:store store}})
                         wrap-request-logging
                         wrap-correct-content-type)
                     {:port port :join? false})))

(comment ;; interactive dev
  (do
    (.stop jetty-server)
    (def jetty-server (-main))))
