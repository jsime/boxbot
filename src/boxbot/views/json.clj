(ns boxbot.views.json
  (:require [cheshire.core :as json]))

(defn- response
  [code payload headers]
  {:status code
   :body (json/encode payload)
   :headers (merge headers {"Content-Type" "application/json"})})

(defn ok
  [payload & headers]
  (response 200 (merge {:msg "Success"} payload) headers))

(defn bad-request
  [payload & headers]
  (response 400 payload headers))

(defn forbidden
  [payload & headers]
  (response 403 payload (merge headers {"WWW-Authenticate" "Token"})))

(defn not-found
  [payload & headers]
  (response 404 payload headers))
