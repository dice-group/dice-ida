(ns generator
  (:require [librarian.generator.core :as gen]
            [librarian.model.io.scrape :as scrape]
            [librarian.model.syntax :refer [instanciate instances->tx]]
            [librarian.model.concepts.call-parameter :as call-parameter]
            [librarian.model.concepts.call-value :as call-value]
            [librarian.model.concepts.call-result :as call-result]
            [librarian.model.concepts.basetype :as basetype]
            [librarian.model.concepts.role-type :as role-type]
            [librarian.model.concepts.semantic-type :as semantic-type]
            [repl-tools :as rt]))

(defn gen-test*
  ([ds init]
   (gen-test* ds init 10))
  ([ds init limit]
   (let [scrape (scrape/read-scrape ds)
         search-state (gen/initial-search-state scrape init)
         succs (iterate gen/continue-search search-state)
         succs (take limit succs)]
     (time (doall succs))
     (time (rt/show-search-state (or (some #(when (:goal %) %) succs)
                                     (last succs)
                                  :show-patterns false))))))

(defn- goal-init-tx
  [inputs goals]
  (instances->tx (concat (mapv (fn [input]
                                 (instanciate call-result/call-result
                                   :datatype [(instanciate role-type/role-type
                                                :id input)]))
                               inputs)
                         (mapv (fn [goal]
                                 (instanciate call-parameter/call-parameter
                                   :datatype [(instanciate role-type/role-type
                                                :id goal)]))
                               goals))))

(defmulti gen-test (fn [t & args] t))

(defmethod gen-test :base
  [_ & args]
  (apply gen-test*
         "libs/scikit-learn-cluster"
         (instances->tx [(instanciate call-value/call-value
                           :value 123
                           :datatype [(instanciate basetype/basetype
                                        :name "str")
                                      (instanciate semantic-type/semantic-type
                                        :key "foo"
                                        :value "bar")
                                      (instanciate role-type/role-type
                                        :id :result)])
                         (instanciate call-parameter/call-parameter
                           :datatype [(instanciate basetype/basetype
                                        :name "int")])])
         args))

(defmethod gen-test :goal
  [_ & args]
  (apply gen-test*
         "libs/scikit-learn-cluster"
         (goal-init-tx [:dataset] [:labels])
         args))
