(ns ^{:doc "Server web to expose the response to problems"}
  cs2013.web
  (:require [compojure
             [core        :refer [defroutes GET PUT POST DELETE ANY]]
             [handler     :refer [site api]]
             [route                            :as route]]
            [environ.core :refer [env]]
            [ring.middleware.stacktrace        :as trace]
            [ring.middleware.session.cookie    :as cookie]
            [ring.adapter.jetty                :as jetty]
            [clojure.tools.trace :only [trace] :as t]
            [clojure.java.io                   :as io]
            [clojure.data.json                 :as json]
            [clojure.string                    :as str]
            [cs2013
             [input-utils                      :as utils]
             [mail                             :as mail]
             [response                         :as r]
             [operations                       :as o]
             [middleware                       :as m]
             [enonce1                          :as e1]
             [enonce2                          :as e2]]))

;; small trick when using demulti (not for prod)
(if (-> env :production not)
  (def deal-with-query nil)
  (def deal-with-operation nil))

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Answering queries 'q='
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti deal-with-query identity)

(defmethod deal-with-query "Quelle est ton adresse email" [_]                                                             (mail/my))
(defmethod deal-with-query "Es tu abonne a la mailing list(OUI/NON)" [_]                                                  "OUI")
(defmethod deal-with-query "Es tu heureux de participer(OUI/NON)" [_]                                                     "OUI")
(defmethod deal-with-query "Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)" [_]               "OUI")
(defmethod deal-with-query "Est ce que tu reponds toujours oui(OUI/NON)" [_]                                              "NON")
(defmethod deal-with-query "As tu bien recu le premier enonce(OUI/NON)" [_]                                               "OUI")
(defmethod deal-with-query "As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)" [_] "QUELS_BUGS")
(defmethod deal-with-query "As tu bien recu le second enonce(OUI/NON)" [_]                                                "OUI")
(defmethod deal-with-query "As tu copie le code de ndeloof(OUI/NON/JE_SUIS_NICOLAS)" [_]                                  "NON")

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; registering bodies problems inside atom
;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; not perfect because we lost the registered post requests as each deployment but better than nothing at the moment
(def ^{:doc "post bodies registered"}
  bodies (atom {}))

;; a route to expose the problems registered
(defmethod deal-with-query "enonces" [_] (-> @bodies pr-str))

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; query operations
;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; this function's job is to make the resource input compliant with the namespace operations and the result compliant with the client's expected format
(defmethod deal-with-query :default
  [q]
  (-> q
      (str/replace \space \+)               ;; space is +
      (str/replace \, \.)                   ;; , is .
      o/compute-infix-operation-from-string
      str                                   ;; we need a string
      (str/replace \. \,)))                 ;; expected decimal separator is , and not .

(defn- read-post-body
  "Just reading the body"
  [body encoding]
  (slurp body :encoding encoding))

(defn- read-post-body-trace-and-register
  "One function to deal with body/original-body (trace, register in atom, etc...)"
  [{:keys [body character-encoding]} key]
  (let [b (read-post-body body character-encoding)]
    (t/trace "body: " b)
    (swap! bodies #(update-in % [key] (fn [_] b)))
    b))

(defn- read-json-post-body
  "One function to deal with body/original-body (trace, register in atom, etc...)"
  [{:keys [body character-encoding]}]
  (-> body (read-post-body character-encoding) json/read-str))

;; the main routing
(defroutes app
  ;; play with remote repl
  (ANY "/repl" {:as req}
       (m/drawbridge req))

  ;; deal with questions
  (GET "/" [q]
       (if q
         (-> q deal-with-query r/body-response)
         (r/body-response "This is a server for code story, nothing to see here.")))

  ;; reception of the problem
  (POST "/enonce/:n" [n :as req]
        (let [key-store (-> "enonce-" (str n) keyword)]
          (-> req (read-post-body-trace-and-register key-store) r/post-body-response)))

  ;; first problem
  (GET "/scalaskel/change/:n" [n]
       (-> n
           read-string
           e1/decomposition           ;; main algo for first problem
           json/write-str
           r/json-body-response))

  ;; solution 2
  (POST "/jajascript/optimize" {:as req}
        (-> req
            read-json-post-body
            t/trace
            utils/keyify
            t/trace
            e2/optimize                ;; main algo for second problem
            t/trace
            json/write-str
            t/trace
            r/post-json-response))

  ;; everything else
  (ANY "*" []
       (-> "404.html" io/resource slurp route/not-found)))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))
        ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
        store (cookie/cookie-store {:key (env :session-secret)})]
    (jetty/run-jetty (-> #'app
                         ((if (env :production) m/wrap-error-page trace/wrap-stacktrace))
                         (site {:session {:store store}})
                         m/wrap-request-logging
                         m/wrap-correct-encoding
                         m/wrap-correct-content-type)
                     {:port port :join? false})))

(comment ;; interactive dev
  (.stop jetty-server) ;; trying to stop the first time will crash
  (def jetty-server (-main)))
