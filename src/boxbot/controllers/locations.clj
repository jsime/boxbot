(ns boxbot.controllers.locations
  (:require [boxbot.models.users :as m.users]
            [boxbot.models.locations :as m.locations]
            [clojure.walk :as walk]))

(defn add
  [request]
  (println (:identity request))
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/find-by-id (:sub (:identity request)))]
    (if user
      (if (m.locations/create user data)
        {:status 200 :body "Success"}
        {:status 400 :body "Could not add location"})
      {:status 403 :body "Not authorized"})))
