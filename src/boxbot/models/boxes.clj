(ns boxbot.models.boxes
  (:require [korma.core :as k]))

(k/defentity boxes
  (k/pk :box_id)
  (k/table :boxes)
  (k/entity-fields :name :description))

(defn all
  []
  (k/select boxes))
