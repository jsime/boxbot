(ns boxbot.models.users
  (:require [korma.core :as k]
            [clojure.string :as string]
            [clojurewerkz.scrypt.core :as sc]
            [boxbot.entities :as e]))

(defn create
  [data]
  (let [user (select-keys data [:name :email])]
    (k/insert e/users
      (k/values
        (assoc user :password (sc/encrypt (:password data) 16384 8 1))))))

(defn find-by-email
  [email]
  (first (k/select e/users
            (k/where {:email (string/lower-case email)}))))

(defn find-by-id
  [user-id]
  (first (k/select e/users
            (k/where {:user_id user-id}))))

(defn verify-login
  [email password]
  (let [user (first (k/select e/users
                      (k/where {:email (string/lower-case email)})))]
    (if (and user (sc/verify password (:password user)))
      user)))

(defn update-password
  [user password]
  (let [h (sc/encrypt password 16384 8 1)]
    (if h
      (assoc user :password h)
      user)))
