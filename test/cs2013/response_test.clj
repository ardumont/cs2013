(ns cs2013.response-test
  (:use [midje.sweet :only [fact future-fact]]
        [cs2013.response]))

(fact
 (body-response "body-message")      => {:status 200 :headers {"Content-Type" "text/plain"} :body "body-message"}

 (post-body-response "body-message") => {:status 201 :headers {"Content-Type" "text/plain"} :body "body-message"})
