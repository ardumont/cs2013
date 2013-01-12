(defproject cs2013 "1.0.0-SNAPSHOT"
  :description "Code story 2013 - web server to expose answers to questions."
  :url "http://serene-spire-2229.herokuapp.com/"
  :license {:name "Eclipse licence"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure       "1.4.0"]
                 [compojure                 "1.1.3"]
                 [ring/ring-jetty-adapter   "1.1.6"]
                 [ring/ring-devel           "1.1.0"]
                 [ring-basic-authentication "1.0.1"]
                 [environ                   "0.2.1"]
                 [com.cemerick/drawbridge   "0.0.6"]
                 [clj-http                  "0.6.3"]
                 [org.clojure/tools.trace   "0.7.3"]
                 [org.clojure/data.json     "0.2.0"]
                 [midje                     "1.4.0"]]
  :dev-dependencies [[lein-midje "2.0.4"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]]
  :hooks [environ.leiningen.hooks]
  :profiles {:production {:env {:production true}}})
