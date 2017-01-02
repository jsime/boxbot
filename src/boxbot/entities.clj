(ns boxbot.entities
  (:require [korma.core :as k]))

(k/defentity users
  (k/pk :user_id)
  (k/table :users)
  (k/entity-fields :user_id :name :email :password :verify_token :verified_at :created_at))

(k/defentity locations
  (k/pk :location_id)
  (k/table :locations)
  (k/entity-fields :location_id :parent_id :user_id :name :description :created_at))

(k/defentity boxes
  (k/pk :box_id)
  (k/table :boxes)
  (k/entity-fields :box_id :user_id :location_id :name :description :created_at))
