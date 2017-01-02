(ns boxbot.models.boxes
  (:require [korma.core :as k]
            [boxbot.entities :as e]))

(defn all
  []
  (k/select e/boxes))
