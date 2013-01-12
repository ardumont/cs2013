(ns cs2013.response-test
  (:use [midje.sweet :only [fact future-fact]]
        [cs2013.response]))

(fact
  (with-status-content-type-message 200 "some-headers" "body-message") => {:status 200 :headers {"Content-Type" "some-headers"} :body "body-message"}

  (body-response "body-message")      => {:status 200 :headers {"Content-Type" "text/plain"} :body "body-message"}

  (post-body-response "body-message") => {:status 201 :headers {"Content-Type" "text/plain"} :body "body-message"}

  (json-body-response "body-message") => {:status 200 :headers {"Content-Type" "application/json"} :body "body-message"})
