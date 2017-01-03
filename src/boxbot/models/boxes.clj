(ns boxbot.models.boxes
  (:require [korma.core :as k]
            [boxbot.entities :as e]))

(defn create
  [user data]
  (let [box (select-keys data [:name :description])
        loc-id (try (Integer. (:location_id data)) (catch Exception e))
        loc (if loc-id (first (k/select e/locations
                         (k/where {:user_id (:user_id user)
                                   :location_id loc-id}))))]
    (if (and loc-id (nil? loc))
      (throw (Exception. "invalid location ID")))
    (k/insert e/boxes
      (k/values
        (assoc box :user_id (:user_id user)
                   :location_id loc-id)))))
