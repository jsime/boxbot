(ns boxbot.models.boxes
  (:require [korma.core :as k]))

(k/defentity boxes
  (k/pk :box_id)
  (k/table :boxes)
  (k/entity-fields :box_id :user_id :location_id :name :description :created_at))

(defn all
  []
  (k/select boxes))
