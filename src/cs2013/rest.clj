(ns cs2013.rest
  (:require [clj-http.client   :as c]
            [clojure.string    :as s]))

(def urls
  {:local "http://localhost:5000/"                    ;; foreman start
   :remote "http://serene-spire-2229.herokuapp.com/"}) ;; git push heroku master

(defn url
  [] (:local urls))

(defn query
  "Query the hdt utility."
  [method path & [opts]] (c/request
                          (merge {:method     method
                                  :url        (format "%s%s" (url) path)
                                  :accept     :plain}
                                 opts)))

(comment
  (url)
  (query :get "?q=Quel+est+ton+adresse+email")
  (c/get "http://localhost:5000/?q=Quelle+est+ton+adresse+email")
  ;; ...
  (c/post "http://localhost:5000/enonce/1" {:body "some-data"}))

;; (h/load-hook #'wikeo-query #'query #'post-cnt)
;; (comment (h/unload-hook #'post-cnt))
