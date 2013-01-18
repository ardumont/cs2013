(ns ^{:doc "rest client for the application"}
  cs2013.rest
  (:require [clj-http.client   :as c]
            [clojure.string    :as s]))

(def urls
  {:denis "http://lit-taiga-5126.herokuapp.com"
   :local "http://localhost:5000"                     ;; foreman start
   :remote "http://serene-spire-2229.herokuapp.com"}) ;; git push heroku master

(defn url []
  (:denis urls))

(defn query
  "Query the server."
  [method path & [opts]] (c/request
                          (merge {:method     method
                                  :url        (format "%s%s" (url) path)
                                  :accept     (if (:accept opts) (:accept opts) :plain)}
                                 opts)))

(comment ;; to check manually that all is good
  (url)
  (query :get "/")

  (query :get "/?nnq=Quelle+est+ton+adresse+email")
  (query :get "/?q=Es+tu+abonne+a+la+mailing+list(OUI/NON)")
  (query :get "/?q=Es+tu+heureux+de+participer(OUI/NON)")
  (query :get "/?q=Es+tu+pret+a+recevoir+une+enonce+au+format+markdown+par+http+post(OUI/NON)")
  (query :get "/?q=Est+ce+que+tu+reponds+toujours+oui(OUI/NON)")
  (query :get "/?q=As+tu+bien+recu+le+premier+enonce(OUI/NON)")
  (query :get "/?q=As+tu+passe+une+bonne+nuit+malgre+les+bugs+de+l+etape+precedente(PAS_TOP/BOF/QUELS_BUGS)")
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

  (query :get "/?q=enonces")

  (query :get "/scalaskel/change/1" {:accept :json})
  (query :get "/scalaskel/change/14" {:accept :json})
  (query :get "/?q=1+1")
  (query :get "/?q=2*2")
  (query :get "/?q=(1+9)*9")
  (query :get "/?q=(1+2)*2")
  (query :get "/?q=(1+2)/2")
  (query :get "/?q=(1+2+3+4+5+6+7+8+9+10)*2"))

(defn post-query-json
  "Query the server for posting a json body"
  [path body & [opts]]
  (c/request
   (merge {:method       :post
           :url          (format "%s%s" (url) path)
           :body         (c/json-encode body)
           :content-type :json
           :accept       :json
           :as           :json}
          opts)))

(comment
  (post-query-json "/jajascript/optimize"
                   [{:VOL "MONAD42"  :DEPART 0 :DUREE 5 :PRIX 10}
                    {:VOL "META18"   :DEPART 3 :DUREE 7 :PRIX 14}
                    {:VOL "LEGACY01" :DEPART 5 :DUREE 9 :PRIX 8}
                    {:VOL "YAGNI17"  :DEPART 5 :DUREE 9 :PRIX 7}])

  (post-query-json "/jajascript/optimize"
                   '({:VOL "AF1" :DEPART 0 :DUREE 1 :PRIX 2}
                     {:VOL "AF2" :DEPART 4 :DUREE 1 :PRIX 4}
                     {:VOL "AF3" :DEPART 2 :DUREE 1 :PRIX 6}))

  {:trace-redirects ["http://lit-taiga-5126.herokuapp.com/jajascript/optimize"], :request-time 347, :status 201, :headers {"content-length" "38", "date" "Fri, 18 Jan 2013 09:23:48 GMT", "cache-control" "no-store", "content-type" "application/json;charset=ISO-8859-1", "server" "Jetty(7.6.1.v20120215)", "connection" "keep-alive"}, :body {:gain 12, :path ["AF1" "AF3" "AF2"]}}

  {:trace-redirects ["http://serene-spire-2229.herokuapp.com/jajascript/optimize"], :request-time 325, :status 201, :headers {"content-length" "38", "date" "Fri, 18 Jan 2013 09:24:32 GMT", "cache-control" "no-store", "content-type" "application/json;charset=ISO-8859-1", "server" "Jetty(7.6.1.v20120215)", "connection" "keep-alive"}, :body {:gain 12, :path ["AF1" "AF3" "AF2"]}})
