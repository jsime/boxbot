(ns boxbot.controllers.auth
  (:require [buddy.sign.jwt :as jwt]
            [boxbot.config :as config]
            [boxbot.models.users :as m.users]
            [boxbot.views.json :as v]
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
    (jwt/sign claims config/jws-secret)))

(defn login
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/verify-login (:email data) (:password data))]
    (if user
      (let [token (gen-token user)]
        (v/ok {:token token}))
      (v/bad-request {:msg "Invalid credentials"}))))

(defn register
  [request]
  (let [data (walk/keywordize-keys (:form-params request))
        user (m.users/create data)]
    (if user
      (v/ok {:user user})
      (v/bad-request {:msg "Could not register account"}))))
