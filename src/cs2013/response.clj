(ns ^{:doc "A namespace to deal with the response to client request"}
  cs2013.response)

(defn with-status-content-type-message
  "Answering a request with message and content-type"
  [s h m]
  {:status s
   :headers {"Content-Type" h}
   :body m})

(defn body-response
  "Answering request in text/plain with the message"
  [m]
  (with-status-content-type-message 200 "text/plain" m))

(defn post-body-response
  "Answering request to post"
  [m]
  (with-status-content-type-message 201 "text/plain" m))

(defn json-body-response
  "Answer a request with json mime type and the message m"
  [m]
  (with-status-content-type-message 200 "application/json" m))

(defn post-json-response
  "Answer a request with json mime type and the message m"
  [m]
  (with-status-content-type-message 201 "application/json" m))
