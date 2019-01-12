(ns lib-scraper.helpers.transaction
  (:require [datascript.core :as d])
  (:refer-clojure :exclude [merge]))

(defn add-attr
  ([attr]
   (add-attr attr identity))
  ([attr f]
   (fn [db id]
     (if-let [v (f (if (d/db? db) (d/entity db id) db))]
       [[:db/add id attr v]]
       []))))

(defn merge
  [& fns]
  (let [fns (disj (set fns) nil)]
    (case (count fns)
      0 (constantly [])
      1 (first fns)
      (let [calls (mapv #(vector :db.fn/call %) fns)]
        (fn [db & args]
          (mapv #(into % args) calls))))))

(defn merge-direct
  [& fns]
  (let [fns (disj (set fns) nil)]
    (case (count fns)
      0 (constantly [])
      1 (first fns)
      (fn [& args]
        (mapcat #(apply % args) fns)))))

(defn tempid?
  ^Boolean [x]
  (or (and (number? x) (neg? x)) (string? x)))

(defn replace-id
  [from to txs]
  (map (fn [tx]
         (cond
           (sequential? tx)
           (let [[op e a v] tx]
             (if (= op :db/add)
               [op (if (= e from) to e) a v]
               tx))
           (and (map? tx) (= (:db/id tx) from))
           (assoc tx :db/id to)
           :else tx))
       txs))