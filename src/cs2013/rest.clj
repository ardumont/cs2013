(ns cs2013.rest
  (:require [clj-http.client   :as c]
            [clojure.string    :as s]))

(def urls
  {:local "http://localhost:5000/"                    ;; foreman start
   :remote "http://serene-spire-2229.herokuapp.com/"}) ;; git push heroku master

(defn url
  [] (:remote urls))

(defn query
  "Query the hdt utility."
  [method path & [opts]] (c/request
                          (merge {:method     method
                                  :url        (format "%s%s" (url) path)
                                  :accept     :plain}
                                 opts)))

(comment
  (url)
  (c/get "http://localhost:5000/?q=Quelle+est+ton+adresse+email")
  (query :get "?q=Quelle+est+ton+adresse+email")
  (query :get "?q=Es+tu+abonne+a+la+mailing+list(OUI/NON)")
  (query :get "?q=Es+tu+heureux+de+participer(OUI/NON)")
  (query :get "?q=Es+tu+pret+a+recevoir+une+enonce+au+format+markdown+par+http+post(OUI/NON)")
  (query :get "?q=Est+ce+que+tu+reponds+toujours+oui(OUI/NON)")
  (query :get "?q=As+tu+bien+recu+le+premier+enonce(OUI/NON)")
  ;; ...
  (query :post "/enonce/1" {:body "some-data" :headers {"Content-type" ""}})
  ;; ko request remotely (because of the content type)
  (query :post "/enonce/1" {:body "some-data" :headers {"Content-type" "application/x-www-form-urlencoded"}})
  )

;; (h/load-hook #'wikeo-query #'query #'post-cnt)
;; (comment (h/unload-hook #'post-cnt))
