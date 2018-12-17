(ns lib-scraper.model.concepts.parameter)

(def spec {::name {:db/type :db.type/string
                   :db/doc "Name of the parameter."}
           ::position {:db/type :db.type/long
                       :db/doc "Position of the parameter."}})