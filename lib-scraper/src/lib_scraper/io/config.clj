(ns lib-scraper.io.config
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :as w]
            [hickory.select]
            [lib-scraper.model.core :as m]
            [lib-scraper.helpers.zip :as hzip]
            [lib-scraper.helpers.predicate :as predicate]
            [lib-scraper.scraper.match :as match])
  (:import (java.util.regex Pattern)))

(s/def ::config-outer (s/cat :defscraper #(= % 'defscraper)
                             :name #(or (string? %) (symbol? %))
                             :config (s/* any?)))

(s/def ::ecosystem (s/and keyword?
                          #(contains? m/ecosystems %)
                          (s/conformer m/ecosystems)))

(s/def ::seed string?)
(s/def ::max-pages int?)
(s/def ::max-depth int?)

(defn- parse-should-visit
  [expr]
  (cond
    (fn? expr) expr
    (or (string? expr) (instance? Pattern expr))
    (match/match-url expr)
    :else
    (predicate/parse {'require-classes match/require-classes
                      'match-url match/match-url}
                     expr)))

(s/def ::should-visit (s/and (s/conformer parse-should-visit) fn?))

(s/def ::hook (s/and (s/keys :opt-un [::trigger ::selector
                                      ::concept
                                      ::ref-to-trigger
                                      ::ref-from-trigger
                                      ::attribute
                                      ::value
                                      ::transform
                                      ::pattern])))

(s/def ::trigger (s/and (s/or :single keyword?
                              :multiple (s/coll-of keyword?))
                        (s/conformer second)))
(s/def ::concept keyword?)
(s/def ::ref-to-trigger keyword?)
(s/def ::ref-from-trigger keyword?)
(s/def ::attribute keyword?)
(s/def ::value (partial contains? #{:content :trigger-index}))
(s/def ::pattern keyword?)
(s/def ::hooks (s/coll-of ::hook))
(s/def ::patterns (s/map-of keyword? ::hook))

(def select-kws (ns-publics 'hickory.select))

(s/def ::spread-op (partial contains? (set (keys hzip/step-types))))
(s/def ::limit int?)
(s/def ::select (s/and seq?
                       (s/conformer (fn [form]
                                      (eval (w/postwalk-replace select-kws form))))))
(s/def ::spread
       (s/and (s/or :basic ::spread-op
                    :complex (s/and (s/cat :spread-op ::spread-op
                                           :args (s/keys* :opt-un [::select ::limit]))
                                    (s/conformer (fn [{:keys [spread-op args]}]
                                                   (into [spread-op] (flatten (seq args)))))))
              (s/conformer second)))
(s/def ::selector-part (s/and (s/or :spread ::spread
                                    :select ::select)
                              (s/conformer second)))
(s/def ::selector (s/and seqable?
                         (s/coll-of ::selector-part)))

(s/def ::fn-form (s/and seq?
                        #(contains? #{'fn* 'fn} (first %))
                        (s/conformer eval)))
(s/def ::pattern-fn (s/and #(instance? Pattern %)
                           (s/conformer (fn [p] #(re-find p %)))))
(s/def ::transform (s/and (s/or :fn fn?
                                :fn-form ::fn-form
                                :pattern ::pattern-fn)
                          (s/conformer second)))


(s/def ::config (s/and (s/keys* :req-un [::ecosystem
                                         ::seed
                                         ::should-visit
                                         ::hooks]
                                :opt-un [::patterns
                                         ::max-pages
                                         ::max-depth])
                       (s/every-kv keyword? any?)))

(defn defscraper*
  [name config]
  (let [conformed (s/conform ::config config)]
    (if (s/invalid? conformed)
      (throw (Exception. (str "Invalid scraper configuration.\n"
                              (s/explain-str ::config config))))
      (assoc conformed :name (str name)))))

(defmacro defscraper
  [name & config]
  `(def ~name ~(defscraper* name config)))

(defn read-config
  [path]
  (binding [*read-eval* false]
    (let [config (read-string (slurp path))
          {:keys [name config]} (s/conform ::config-outer config)]
      (defscraper* name config))))
