(ns auto-core.data.sequence "Helpers on sequence manipulation")

(defn idx-of
  "Returns the index of the first found value in the sequence"
  [v value]
  (ffirst (filter #(= value (second %)) (map-indexed vector v))))

(defn idx-of-pred
  "Same as idx-of but with a predicate"
  [v pred]
  (when (and pred (fn? pred))
    (ffirst (filter #(pred (second %)) (map-indexed vector v)))))

(defn trim-leading-nil
  "Remove `nil` values at the end of a sequence `s`"
  [s]
  (loop [s s
         i 40] ;; For security reason
    (when-not (pos? i) (throw (ex-info "Infinite loop detected" {})))
    (if (and (seq s) (nil? (last s))) (recur (butlast s) (dec i)) s)))

(defn index-of
  "Returns position of the first element matching predicate `pred` in the sequence.

  Pred should be `(pred x)`."
  [coll pred]
  (let [idx? (fn [i a] (when (pred a) i))] (first (keep-indexed idx? coll))))

(defn indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))

(defn positions
  "Returns a lazy sequence containing the positions at which pred is true for items in coll."
  [pred coll]
  (for [[idx elt] (indexed coll) :when (pred elt)] idx))
