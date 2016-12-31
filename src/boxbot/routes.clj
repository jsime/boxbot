(ns boxbot.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [buddy.auth.accessrules :refer (success error)]
            [boxbot.controllers.boxes :as c.boxes]
            [boxbot.controllers.locations :as c.locations]
            [boxbot.controllers.auth :as c.auth]))

(defn home
  [request]
  {:status 200
   :body "<h1>Hello world!</h1>"})

(defn as-int
  [p]
  (let [c (re-find #"\d+" p)]
    (if c
      (Integer. c)
      nil)))

(defroutes routes
  (GET "/" [] home)

  (POST "/locations/add" [] c.locations/add)
  (GET "/locations" [] c.locations/user-locations)
  (GET "/location/:id" [id :<< as-int :as r] (c.locations/user-location r id))

  (GET "/boxes" [] c.boxes/boxes)
  (GET "/box/:id" [id :as r] (c.boxes/box r id))

  (POST "/login" [] c.auth/login)
  (POST "/register" [] c.auth/register)

  (not-found "<h1>Page not found</h1>"))

;; Authentication rule handlers
(defn any-access
  [request]
  (success))

(defn authenticated-access
  [request]
  (if (:identity request)
    (success)
    (error "Invalid authentication provided for protected resource")))

(defn admin-access
  [request]
  (error "Handler admin-access not implemented"))

;; Authentication requirement rules
(def rules [{:pattern #"^/admin/.*"
             :handler admin-access}
            {:uris ["/" "/login" "/register"]
             :handler any-access}
            {:pattern #"^/.+"
             :handler authenticated-access}])

;; General unauth access handler
(defn on-error
  [request value]
  {:status 403
   :headers {"X-Reason" value}
   :body "Not authorized"})
