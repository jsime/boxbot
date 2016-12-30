(ns boxbot.controllers.auth
  (:require [buddy.sign.jwt :as jwt]
            [cheshire.core :as json]
            [boxbot.config :as config]
            [boxbot.models.users :as m.users]
            [clojure.walk :as walk]))

(defn login
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/verify-login (:email data) (:password data))
        token (jwt/sign {:user_id (:user_id user)} config/jws-secret)]
    (println user)
    (if user
      {:status 200
       :body (json/encode {:token token})
       :headers {"Content-Type" "application/json"}}
      {:status 401
       :body (json/encode {:error "Invalid credentials supplied."})
       :headers {"Content-Type" "application/json"}})))

(defn register
  [request]
  (let [data (walk/keywordize-keys (:form-params request))]
    (if (m.users/create data)
      {:status 200 :body "Success"}
      {:status 400 :body "Could not register account"})))
