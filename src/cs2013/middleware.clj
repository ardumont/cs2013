(ns ^{:doc "Middleware"}
  cs2013.middleware
  (:require [clojure.tools.trace :only [trace] :as t]
            [clojure.java.io :as io]
            [environ.core :refer [env]]
            [cemerick.drawbridge :as drawbridge]
            [ring.middleware.session :as session]
            [ring.middleware.basic-authentication :as basic]))

(defn authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(defn wrap-request-logging
  "Log request middleware"
  [handler]
  (fn [req] (-> req t/trace handler)))

(defn wrap-error-page [handler]
  "A middleware to deal with error page"
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (-> "500.html" io/resource slurp)}))))

(defn wrap-correct-content-type [handler]
  "A middleware to fix the forgotten content-type (thus resulting in consuming the body later)"
  (fn [req]
    (if (= "application/x-www-form-urlencoded" (:content-type req))
      (handler (assoc req :content-type "application/json"))
      (handler req))))
