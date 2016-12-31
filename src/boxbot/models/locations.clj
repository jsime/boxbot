(ns boxbot.models.locations
  (:require [korma.core :as k]
            [clojure.string :as string]))

(k/defentity locations
  (k/pk :location_id)
  (k/table :locations)
  (k/entity-fields :location_id :parent_id :user_id :name :description :created_at))

(defn create
  [user data]
  ;; TODO Ensure that user actually owns the location specified in :parent_id
  (let [loc (select-keys data [:parent_id :name :description])]
    (k/insert locations
      (k/values
        (assoc loc :user_id (:user_id user))))))

