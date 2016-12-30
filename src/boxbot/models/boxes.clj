(ns boxbot.models.boxes
  (:require [korma.core :refer :all]
            [boxbot.db :as db]))

(defentity boxes
  (pk :box_id)
  (table :boxes)
  (entity-fields :name :description))

(defn all
  []
  (select boxes))
