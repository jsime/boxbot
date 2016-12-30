(ns boxbot.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [buddy.sign.jwt :as jwt]
            [cheshire.core :as json]
            [boxbot.config :as config]
            [boxbot.controllers.boxes :as c.boxes]
            [boxbot.models.users :as m.users]
            [clojure.walk :as walk]
            [clojure.string :as string]))

(defn home
  [request]
  {:status 200
   :body "<h1>Hello world!</h1>"})

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
  (println request)
  (let [data (walk/keywordize-keys (:form-params request))]
    (if (m.users/create data)
      {:status 200 :body "Success"}
      {:status 400 :body "Could not register account"})))

(defroutes routes
  (GET "/" [] home)

  (GET "/boxes" [] c.boxes/boxes)
  (GET "/box/:id" [id :as r] (c.boxes/box r id))

  (POST "/login" [] login)
  (POST "/register" [] register)

  (not-found "<h1>Page not found</h1>"))
