(defproject lrn_jdbc "0.0.0"
  :description "Learn Database Access via a Clojure REPL"
  :url "http://example.com/FIXME"
  :license {:name "Share alike and Credit me"
            :url "creative commons org"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.7.1"]
                 [ring/ring-json "0.5.0"]
                 [compojure "1.6.1"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]]
  :repl-options {:init-ns lrn_jdbc2.core}
  :main lrn_jdbc.core)
