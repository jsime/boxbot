(ns boxbot.controllers.locations
  (:require [boxbot.models.users :as m.users]
            [boxbot.models.locations :as m.locations]
            [cheshire.core :as json]
            [clojure.walk :as walk]))

(defn add
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/find-by-id (:sub (:identity request)))]
    (if user
      (if (m.locations/create user data)
        {:status 200 :body "Success"}
        {:status 400 :body "Could not add location"})
      {:status 403 :body "Not authorized"})))

(defn user-locations
  [request]
  (let [locs (m.locations/user-locations (:sub (:identity request)))]
    {:status 200
     :body (json/encode locs)
     :headers {"Content-Type" "application/json"}}))

(defn user-location
  [request loc-id]
  (let [loc (m.locations/user-location (:sub (:identity request)) loc-id)]
    (if loc
      {:status 200
       :body (json/encode loc)
       :headers {"Content-Type" "application/json"}}
      {:status 404})))
