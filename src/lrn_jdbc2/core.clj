(ns lrn_jdbc2.core
  (:use [compojure.core :only (defroutes GET)]
        [ring.adapter.jetty :as ring]
        )
        
  (:require [clojure.java.jdbc :as jdbc]
            [compojure.handler :as handler]
            [hiccup.page :as page]
            )
  (:gen-class))

(def db-spec "postgresql://localhost:5432/fruit")

(defn migrated? []
  (-> (jdbc/query db-spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='fruit'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (jdbc/db-do-commands db-spec
                        (jdbc/create-table-ddl
                         :fruit
                         [:id :serial "PRIMARY KEY"]
                         [:name :varchar "NOT NULL"]
                         [:appearance :varchar "NOT NULL"]
                         [:cost :numeric "NOT NULL"]
                         ))
    (println " done")))

; this is the part I KEEP getting wrong
(defn map-tag [tag xs]
  (map (fn [x] [tag x]) xs))

(defn base-layout [title body]
  [:html 
   [:head
    [:title title]]
   [:body
    body]]
  )

(defn query-all [] 
  "poor man's controller - returns list of hashmaps"
  (let [db-resp (jdbc/query db-spec ["SELECT name,appearance,cost FROM fruit"])]
            (list [:table [:tr (map-tag :th ["Name" "Color" "Cost"])(map-tag :tr (map (fn [row] (map-tag :td [(:name row) (:appearance row) (:cost row)])) db-resp))]])
              ) 
    )

(defn index []
  (page/html5 (base-layout "Fruit" (query-all))))

(defroutes routes
  (GET "/" [] (index))
  )

(def application (handler/site routes))

(defn start [port]
  (run-jetty application {:port port
                          :join? false}))

(defn -main []
  (migrate)
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (start port)))
