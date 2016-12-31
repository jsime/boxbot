(ns boxbot.core
  (:require [ring.adapter.jetty :refer (run-jetty)]
            [ring.logger :as logger]
            [ring.middleware.params :refer (wrap-params)]
            [ring.middleware.stacktrace :refer (wrap-stacktrace-log)]
            [buddy.auth.middleware :refer (wrap-authentication wrap-authorization)]
            [buddy.auth.accessrules :refer (wrap-access-rules)]
            [boxbot.config :as config]
            [boxbot.routes :as r]
            [boxbot.db :as db])
  (:gen-class))

(defn -main
  [& args]
  (config/load-config args)
  (db/connect)
  (let [options {:rules r/rules :on-error r/on-error}
        app     (-> r/routes
                  (wrap-stacktrace-log)
                  (wrap-access-rules options)
                  (wrap-authorization config/auth-backend)
                  (wrap-authentication config/auth-backend)
                  (wrap-params)
                  (logger/wrap-with-logger))]
    (run-jetty app config/jetty-opts)))
