(ns boxbot.entities
  (:require [korma.core :as k]))

(declare users locations boxes items)

(k/defentity users
  (k/pk :user_id)
  (k/table :users)
  (k/entity-fields :user_id :name :email :password :verify_token :verified_at :created_at)
  (k/has-many locations))

(k/defentity locations
  (k/pk :location_id)
  (k/table :locations)
  (k/entity-fields :location_id :parent_id :user_id :name :description :created_at)
  (k/belongs-to users {:fk :user_id})
  (k/has-many boxes))

(k/defentity boxes
  (k/pk :box_id)
  (k/table :boxes)
  (k/entity-fields :box_id :user_id :location_id :name :description :created_at)
  (k/belongs-to users {:fk :user_id})
  (k/belongs-to locations {:fk :location_id})
  (k/has-many items))

(k/defentity items
  (k/pk :item_id)
  (k/table :items)
  (k/entity-fields :item_id :user_id :box_id :name :description :created_at :boxed_at)
  (k/belongs-to users {:fk :user_id})
  (k/belongs-to boxes {:fk :box_id}))
