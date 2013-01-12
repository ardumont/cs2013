(ns ^{:doc "A namespace to deal with the response to client request"}
  cs2013.response)

(defn body-response
  "Answering request in text/plain with the message"
  [m]
  (with-status-content-type-message 200 "text/plain" m))

(defn post-body-response
  "Answering request to post"
  [m]
  (with-status-content-type-message 201 "text/plain" m))

(defn with-status-content-type-message
  "Answering a request with message and content-type"
  [s h m]
  {:status s
   :headers {"Content-Type" h}
   :body m})
