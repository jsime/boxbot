(ns boxbot.models.users
  (:require [korma.core :as k]
            [clojure.string :as string]
            [clojurewerkz.scrypt.core :as sc]))

(k/defentity users
  (k/pk :user_id)
  (k/table :users)
  (k/entity-fields :user_id :name :email :password :verify_token :verified_at :created_at))

(defn create
  [data]
  (let [user (select-keys data [:name :email])]
    (k/insert users
      (k/values
        (assoc user :password (sc/encrypt (:password data) 16384 8 1))))))

(defn single-by-email
  [email]
  (first (k/select users
            (k/where {:email (string/lower-case email)}))))

(defn single-by-id
  [user-id]
  (first (k/select users
            (k/where {:user_id user-id}))))

(defn verify-login
  [email password]
  (let [user (first (k/select users
                      (k/where {:email (string/lower-case email)})))]
    (if (and user (sc/verify password (:password user)))
      user)))

(defn update-password
  [user password]
  (let [h (sc/encrypt password 16384 8 1)]
    (if h
      (assoc user :password h)
      user)))
