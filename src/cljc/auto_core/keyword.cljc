(ns auto-core.keyword
  "Utility functions for keywords"
  (:require
   [clojure.string :as str]))

(defn keywordize
  "Change string to appropriate clojure keyword"
  [s]
  (-> (name s)
      str/lower-case
      (str/replace "_" "-")
      (str/replace "." "-")
      (keyword)))

(defn sanitize-map-keys
  "Changes all keywords in a map to appropriate clojure keys."
  [map]
  (reduce-kv (fn [acc key value]
               (let [new-key (if (keyword? key) (keywordize key) key)
                     new-val (if (map? value) (sanitize-map-keys value) value)]
                 (assoc acc new-key new-val)))
             {}
             map))

(defn unkeywordize
  "Turns a keyword into a string, return other values as-is

  Params:
  * `v` value"
  [v]
  (if (keyword? v) (name v) v))
