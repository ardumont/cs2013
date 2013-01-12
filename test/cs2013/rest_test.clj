(ns cs2013.rest-test
  (:use [midje.sweet]
        [cs2013.rest]))

(comment ;; you must first launch the local server

  (do
    (.stop jetty-server)
    (def jetty-server (-main)))

  (fact "get"
    (query :get "?q=Es+tu+abonne+a+la+mailing+list(OUI/NON)") => (contains {:status 200 :body "OUI"})
    (query :get "?q=Es+tu+heureux+de+participer(OUI/NON)")  => (contains {:status 200 :body "OUI"})
    (query :get "?q=Es+tu+pret+a+recevoir+une+enonce+au+format+markdown+par+http+post(OUI/NON)")  => (contains {:status 200 :body "OUI"})
    (query :get "?q=Est+ce+que+tu+reponds+toujours+oui(OUI/NON)")  => (contains {:status 200 :body "NON"})
    (query :get "?q=As+tu+bien+recu+le+premier+enonce(OUI/NON)")  => (contains {:status 200 :body "OUI"}))

  (fact "post"
    (query :post "/enonce/1" {:body "some-data-with-x-www-form-url-encoded-and-encoding"
                              :headers {"Content-Type" "application/x-www-form-url-encoded"}
                              :encoding "UTF-8"}) => (contains {:status 201 :body "some-data-with-x-www-form-url-encoded-and-encoding"})

    (query :post "/enonce/1" {:body "some-data-and-encoding"
                              :headers {"Content-Type" "application/x-www-form-url-encoded"}
                              :encoding nil}) => (contains {:status 201 :body "some-data-and-encoding"})

    (query :post "/enonce/1" {:body "some-data-octet-stream"
                              :headers {"Content-Type" "application/octet-stream"}})  => (contains {:status 201 :body "some-data-octet-stream"}))

  (fact "enonce"
    (query :get "/scalaskel/change/1" {:accept :json}) => (contains {:status 200 :body "[{\"foo\":1,\"bar\":0,\"qix\":0,\"baz\":0}]"}))

  (fact "operations simples"
    (query :get "?q=1+1")  => (contains {:status 200 :body "2"})
    (query :get "?q=2*2")  => (contains {:status 200 :body "4"}))

  (fact "operations"
    (query :get "?q=(2*2)+1")  => (contains {:status 200 :body "5.0"})
    (query :get "?q=(1*3)/2")  => (contains {:status 200 :body "1.5"})))
