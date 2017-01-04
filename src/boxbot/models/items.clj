(ns boxbot.models.items
  (:require [korma.core :as k]
            [clj-time.core :as t]
            [clj-time.coerce :refer [to-sql-time]]
            [boxbot.entities :as e]))

(defn create
  [user data]
  (let [item (select-keys data [:name :description])
        box-id (try (Integer. (:box_id data)) (catch Exception e))
        box (if box-id (first (k/select e/boxes
                         (k/where {:user_id (:user_id user)
                                   :box_id box-id}))))]
    (if (and box-id (nil? box))
      (throw (Exception. "invalid box ID")))
    (k/insert e/items
      (k/values
        (assoc item :user_id (:user_id user)
                    :box_id box-id
                    :boxed_at (if box-id (to-sql-time (t/now)) nil))))))
