(ns boxbot.core
  (:require [ring.adapter.jetty :refer (run-jetty)]
            [ring.logger :as logger]
            [ring.middleware.params :refer (wrap-params)]
            [buddy.auth.middleware :refer (wrap-authentication wrap-authorization)]
            [buddy.auth.accessrules :refer (success error wrap-access-rules)]
            [boxbot.config :as config]
            [boxbot.routes :as r]
            [boxbot.db :as db])
  (:gen-class))

;; Authentication rule handlers
(defn any-access
  [request]
  (success))

(defn authenticated-access
  [request]
  (if (:identity request)
    (success)
    (error "Invalid authentication provided for protected resource")))

(defn admin-access
  [request]
  (error "Handler admin-access not implemented"))

;; Authentication requirement rules
(def rules [{:pattern #"^/admin/.*"
             :handler admin-access}
            {:uris ["/" "/login" "/register"]
             :handler any-access}
            {:pattern #"^/.+"
             :handler authenticated-access}])

;; General unauth access handler
(defn on-error
  [request value]
  {:status 403
   :headers {"X-Reason" value}
   :body "Not authorized"})

(defn -main
  [& args]
  (config/load-config args)
  (db/connect)
  (let [options {:rules rules :on-error on-error}
        app     (-> r/routes
                  (wrap-access-rules options)
                  (wrap-authorization config/auth-backend)
                  (wrap-authentication config/auth-backend)
                  (wrap-params)
                  (logger/wrap-with-logger))]
    (run-jetty app config/jetty-opts)))
