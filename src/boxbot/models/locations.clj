(ns boxbot.models.locations
  (:require [korma.core :as k]
            [clojure.string :as string]
            [boxbot.entities :as e]))

(defn create
  [user data]
  ;; TODO Ensure that user actually owns the location specified in :parent_id
  (let [loc (select-keys data [:parent_id :name :description])]
    (k/insert e/locations
      (k/values
        (assoc loc :user_id (:user_id user))))))

(defn user-locations
  [user-id]
  (k/select e/locations
    (k/where {:user_id user-id})))

(defn user-location
  [user-id loc-id]
  (k/select e/locations
    (k/where {:user_id user-id :location_id loc-id})))
