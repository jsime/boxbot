(defproject boxbot "0.1.0"
  :description "A simple API for managing the contents of boxes."
  :url "https://github.com/jsime/boxbot"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.eclipse.jetty/jetty-server "9.4.0.v20161208"]
                 [ring "1.5.0"]
                 [buddy/buddy-auth "1.3.0"]
                 [cheshire "5.6.3"]
                 [com.grammarly/omniconf "0.2.5"]
                 [compojure "1.5.1"]
                 [org.postgresql/postgresql "9.4.1212"]
                 [korma "0.4.3"]
                 [migratus "0.8.32"]
                 [org.slf4j/slf4j-log4j12 "1.7.9"]
                 [clojurewerkz/scrypt "1.2.0"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]
                 [ring-logger "0.7.6"]]
  :plugins [[migratus-lein "0.4.3"]]
  :aot [boxbot.core]
  :main boxbot.core
  :profiles {:uberjar {:aot :all}}

  ;; For use only with a local dummy database for migrations testing.
  ;; Actual deployed migrations will pick up DB connection info from
  ;; the main configuration module.
  :migratus {:store :database
             :migration-dir "migrations"
             :db {:classname "org.postgresql.jdbc.Driver"
                  :subprotocol "postgresql"
                  :subname "//localhost:5432/boxbot"
                  :user "boxbot"
                  :password "boxbot"}})
