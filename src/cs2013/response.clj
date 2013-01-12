(ns ^{:doc "A namespace to deal with the response to client request"}
  cs2013.response)

(defn body-response
  "Answering request in text/plain with the message"
  [m]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body m})

(defn post-body-response
  "Answering request to post"
  [m]
  {:status 201
   :headers {"Content-Type" "text/plain"}
   :body m})
