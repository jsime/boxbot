(ns boxbot.controllers.auth
  (:require [buddy.sign.jwt :as jwt]
            [cheshire.core :as json]
            [boxbot.config :as config]
            [boxbot.models.users :as m.users]
            [clj-time.core :as time]
            [clojure.walk :as walk]
            [trptcolin.versioneer.core :as version]))

(defn- gen-token
  [user]
  (let [issued (time/now)
        expiry (time/plus issued (time/seconds (* 7 86400)))
        claims {:iss (str "boxbot/" (version/get-version "boxbot" "boxbot"))
                :sub (:user_id user)
                :iat issued
                :exp expiry}]
    (jwt/sign claims config/jws-secret {:alg :hs512})))

(defn login
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/verify-login (:email data) (:password data))]
    (if user
      (let [token (gen-token user)]
        {:status 200
         :body (json/encode {:token token})
         :headers {"Content-Type" "application/json"}})
      {:status 401
       :body (json/encode {:error "Invalid credentials supplied."})
       :headers {"Content-Type" "application/json"}})))

(defn register
  [request]
  (let [data (walk/keywordize-keys (:form-params request))]
    (if (m.users/create data)
      {:status 200 :body "Success"}
      {:status 400 :body "Could not register account"})))
