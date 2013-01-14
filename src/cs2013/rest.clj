(ns ^{:doc "rest client for the application"}
  cs2013.rest
  (:require [clj-http.client   :as c]
            [clojure.string    :as s]))

(def urls
  {:local "http://localhost:5000"                     ;; foreman start
   :remote "http://serene-spire-2229.herokuapp.com"}) ;; git push heroku master

(def url (:local urls))

(defn query
  "Query the server."
  [method path & [opts]] (c/request
                          (merge {:method     method
                                  :url        (format "%s%s" url path)
                                  :accept     (if (:accept opts) (:accept opts) :plain)}
                                 opts)))

(comment ;; to check manually that all is good
  (url)
  (query :get "?q=Quelle+est+ton+adresse+email")
  (query :get "?q=Es+tu+abonne+a+la+mailing+list(OUI/NON)")
  (query :get "?q=Es+tu+heureux+de+participer(OUI/NON)")
  (query :get "?q=Es+tu+pret+a+recevoir+une+enonce+au+format+markdown+par+http+post(OUI/NON)")
  (query :get "?q=Est+ce+que+tu+reponds+toujours+oui(OUI/NON)")
  (query :get "?q=As+tu+bien+recu+le+premier+enonce(OUI/NON)")
  (query :get "?q=As+tu+passe+une+bonne+nuit+malgre+les+bugs+de+l+etape+precedente(PAS_TOP/BOF/QUELS_BUGS)")
  ;; ...
  (query :post "/enonce/1" {:body "some-data-with-x-www-form-url-encoded-and-encoding"
                            :headers {"Content-Type" "application/x-www-form-url-encoded"}
                            :encoding "UTF-8"})

  (query :post "/enonce/1" {:body "some-data-with-x-www-form-url-encoded-and-encoding"
                            :headers {"Content-Type" "application/x-www-form-url-encoded"}
                            :encoding "UTF-8"})

  (query :post "/enonce/1" {:body "some-data-and-encoding"
                            :headers {"Content-Type" "application/x-www-form-url-encoded"}
                            :encoding nil})

  (query :post "/enonce/2" {:body "some-data-octet-stream"
                            :headers {"Content-Type" "application/octet-stream"}})

  (query :get "?q=enonces")

  (query :get "/scalaskel/change/1" {:accept :json})
  (query :get "/scalaskel/change/14" {:accept :json})
  (query :get "?q=1+1")
  (query :get "?q=2*2")
  (query :get "?q=(1+9)*9")
  (query :get "?q=(1+2)*2")
  (query :get "?q=(1+2)/2")
  (query :get "?q=(1+2+3+4+5+6+7+8+9+10)*2"))
