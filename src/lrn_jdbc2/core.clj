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


