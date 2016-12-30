(ns boxbot.config
  (:require [buddy.auth.backends :as backends]
            [omniconf.core :as cfg]))

(cfg/define
  {:host {:description "Address on which to listen (default: 0.0.0.0)"
          :type :string
          :default "0.0.0.0"}
   :port {:description "Port on which to listen (default: 3000)"
          :type :number
          :default 3000}
   :db-host {:description "Database host name (default: localhost)"
            :type :string
            :default "localhost"}
   :db-port {:description "Database port (default: 5432)"
             :type :number
             :default 5432}
   :db-name {:description "Database name (default: boxbot)"
             :type :string
             :default "boxbot"}
   :db-user {:description "Database user (default: boxbot)"
             :type :string
             :default "boxbot"}
   :db-pass {:description "Database password"
             :type :string}
   :auth-secret {:description "Secret to use for signing JWTs"
                 :type :string
                 :required true}})

(declare jws-secret auth-backend jetty-opts db-opts)

(defn load-config
  [args]
  (cfg/populate-from-cmd args)
  (when-let [conf (cfg/get :conf)]
    (cfg/populate-from-file conf))
  (cfg/populate-from-properties)
  (cfg/populate-from-env)
  (cfg/verify :quit-on-error true)
  (def jws-secret (cfg/get :auth-secret))
  (def auth-backend (backends/jws {:secret jws-secret}))
  (def jetty-opts {:host (cfg/get :host) :port (cfg/get :port)})
  (def db-opts (reduce #(assoc % %2 (cfg/get %2)) {} [:db-host :db-port :db-name :db-user :db-pass])))
