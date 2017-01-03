(ns boxbot.controllers.boxes
  (:require [boxbot.config :as config]
            [boxbot.models.boxes :as m.boxes]
            [boxbot.models.users :as m.users]
            [boxbot.views.json :as v]
            [clojure.string :as string]
            [clojure.walk :as walk]))

(defn add
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/find-by-id (:sub (:identity request)))]
    (if user
      (try
        (let [box (m.boxes/create user data)]
          (if box
            (v/ok {:box box})))
        (catch Exception e (v/bad-request {:msg "Invalid box data" :err (.getMessage e)})))
      (v/forbidden {:msg "User not found"}))))
