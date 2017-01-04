(ns boxbot.controllers.items
  (:require [boxbot.views.json :as v]
            [boxbot.models.items :as m.items]
            [boxbot.models.users :as m.users]
            [clojure.walk :as walk]))

(defn add
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/find-by-id (:sub (:identity request)))]
    (if user
      (try
        (let [item (m.items/create user data)]
          (if item
            (v/ok {:item item})))
        (catch Exception e (v/bad-request {:msg "Invalid item data" :err (.getMessage e)})))
      (v/forbidden {:msg "User not found"}))))
