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
            [environ.core :refer [env]]))

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
   :body (pr-str m)})

(def deal-with-query nil)
(defmulti deal-with-query =)

(defmethod deal-with-query "Quelle+est+ton+adresse+email"
  [_]
  (body-response (my-mail)))

(defmethod deal-with-query "Quelle+est+ton+adresse+email"
  [_]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (pr-str (my-mail))})

(defroutes app
  (ANY "/repl" {:as req}
       (drawbridge req))
  (GET "/q" [q]
       (deal-with-query q))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))
        ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
        store (cookie/cookie-store {:key (env :session-secret)})]
    (jetty/run-jetty (-> #'app
                         ((if (env :production)
                            wrap-error-page
                            trace/wrap-stacktrace))
                         (site {:session {:store store}}))
                     {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
