(ns boxbot.db
  (:require [korma.db :refer :all]
            [boxbot.config :as config]))

(defn connect
  []
  (defdb pg (postgres {:db (:db-name config/db-opts)
                       :user (:db-user config/db-opts)
                       :password (:db-pass config/db-opts)
                       :host (:db-host config/db-opts)
                       :port (:db-port config/db-opts)
                       :delimiters ""})))
