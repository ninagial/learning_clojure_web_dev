(ns lrn_jdbc2.core
  (:require [clojure.java.jdbc :as jdbc]))

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

(defn map-tag [tag xs]
  (map (fn [x] [tag x]) xs))

(defn query-all [] 
  "poor man's controller - returns list of hashmaps"
  (jdbc/query db-spec ["SELECT * FROM fruit"]))

(defn list-to-table [x]  
  "poor man's view"
  (list [:table [:tr (map-tag :th ["Name" "Appearance" "Cost"])] [:tr (map-tag :td (query-all))]]       
  x))

(defn -main [] 
  (-> 
    (list-to-table (query-all))
    first))
