(ns boxbot.controllers.boxes
  (:require [boxbot.config :as config]
            [boxbot.models.boxes :as m.boxes]
            [clojure.string :as string]))

(defn boxes
  [request]
  {:status 200
   :body (str "Boxes for " (:identity request) ": " (string/join ", " (map #(:name %) (m.boxes/all))))})

(defn box
  [request id]
  {:status 200
   :body (str "Box ID " id " for " (:identity request))})
