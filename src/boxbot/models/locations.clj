(ns boxbot.models.locations
  (:require [korma.core :as k]
            [clojure.string :as string]
            [boxbot.entities :as e]))

(defn create
  [user data]
  (let [loc (select-keys data [:name :description])
        parent-id (try (Integer. (:parent_id data)) (catch Exception e))
        parent (if parent-id (first (k/select e/locations
                               (k/where {:user_id (:user_id user)
                                         :location_id parent-id}))))]
    (if (and parent-id (nil? parent))
      (throw (Exception. "invalid parent location ID")))
    (k/insert e/locations
      (k/values
        (assoc loc :user_id (:user_id user)
                   :parent_id parent-id)))))

(defn user-locations
  [user-id]
  (k/select e/locations
    (k/where {:user_id user-id})))

(defn user-location
  [user-id loc-id]
  (let [loc (first
              (k/select e/locations
                (k/where {:user_id user-id :location_id loc-id})))]
    (if loc
      (assoc loc :parents (k/exec-raw [(str
        "WITH RECURSIVE r AS ("
        "       SELECT  l.*, array[l.location_id] as path, 0 as level"
        "         FROM  locations l"
        "        WHERE  l.parent_id is null"
        "        UNION ALL"
        "       SELECT  l2.*, r.path || l2.location_id as path, r.level + 1 as level"
        "         FROM  locations l2"
        "         JOIN r on l2.parent_id = r.location_id"
        ") SELECT  r2.location_id, r2.name, r2.parent_id, r2.description"
        "    FROM  (SELECT unnest(r.path) FROM r WHERE r.user_id = ? and r.location_id = ?) x(id)"
        "    JOIN  r r2 on (r2.location_id = x.id)"
        "   WHERE  r2.location_id != ?"
        "ORDER BY  r2.level asc") [user-id loc-id loc-id]] :results)))))
