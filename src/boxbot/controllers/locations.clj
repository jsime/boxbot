(ns boxbot.controllers.locations
  (:require [boxbot.models.users :as m.users]
            [boxbot.models.locations :as m.locations]
            [boxbot.views.json :as v]
            [cheshire.core :as json]
            [clojure.walk :as walk]))

(defn add
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/find-by-id (:sub (:identity request)))]
    (if user
      (try
        (let [loc (m.locations/create user data)]
          (if loc
            (v/ok {:location loc})))
        (catch Exception e (v/bad-request {:msg "Invalid location data", :err (.getMessage e)})))
      (v/forbidden {:msg "User not found"}))))

(defn user-locations
  [request]
  (let [locs (m.locations/user-locations (:sub (:identity request)))]
    (v/ok {:locations locs})))

(defn user-location
  [request loc-id]
  (let [loc (m.locations/user-location (:sub (:identity request)) loc-id)]
    (if (> (count loc) 0)
      (v/ok {:location loc})
      (v/not-found {:msg "No such location"}))))
