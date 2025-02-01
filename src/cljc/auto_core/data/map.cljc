(ns auto-core.data.map "Map datastructure utility functions")

;; ********************************************************************************
;; Nested maps manipulation
;; ********************************************************************************

(defn deep-merge
  "Deep merge nested maps.
  When multiple values are found for a key, the value from the last map has higher priority

  This code comes from this [gist](https://gist.github.com/danielpcox/c70a8aa2c36766200a95)"
  [& maps]
  (apply merge-with
         (fn [& args]
           (if (every? #(or (map? %) (nil? %)) args) (apply deep-merge args) (last args)))
         maps))

(defn add-ids
  "For `mm` a map of map, turns `{:foo {}}` to `{:foo {:id :foo}}` so they key of the outer map is found in the inner one."
  ([mm] (add-ids mm :id))
  ([mm id-kw] (into {} (mapv (fn [[k v]] [k (assoc v id-kw k)]) mm))))

;; ********************************************************************************
;; Various
;; ********************************************************************************

(defn remove-nil-vals
  "Remove nil values"
  [m]
  (->> m
       (keep (fn [[k v]] (when-not (nil? v) [k v])))
       (into {})))

(defn map-difference
  [m1 m2]
  (loop [m (transient {})
         ks (concat (keys m1) (keys m2))]
    (if-let [k (first ks)]
      (let [e1 (find m1 k)
            e2 (find m2 k)]
        (cond
          (and e1 e2 (not= (e1 1) (e2 1))) (recur (assoc! m k (e1 1)) (next ks))
          (not e1) (recur (assoc! m k (e2 1)) (next ks))
          (not e2) (recur (assoc! m k (e1 1)) (next ks))
          :else (recur m (next ks))))
      (persistent! m))))


;; ********************************************************************************
;; When keys are integers
;; ********************************************************************************

(defn get-key-or-before
  "Returns the key if it exists in the sorted-map

  Note this is not an efficient way

  Params:
  * `m` the sorted map
  * `n`"
  [^clojure.lang.PersistentTreeMap m n]
  (->> m
       keys
       (take-while (partial >= n))
       last))

(defn get-key-or-after
  "Returns the key if it exists in the sorted-map

  Note this is not an efficient way

  Params:
  * `m` the sorted map
  * `n`"
  [^clojure.lang.PersistentTreeMap m n]
  (->> m
       keys
       (take-while (partial <= n))
       last))
