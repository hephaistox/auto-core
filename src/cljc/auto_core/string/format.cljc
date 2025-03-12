(ns auto-core.string.format
  #?(:cljs (:require
            [goog.string :as gstring]
            [goog.string.format])))

(defn f
  "To format strings across clojure(script)"
  [s & args]
  #?(:clj (apply format s args)
     :cljs (apply gstring/format s args)))

(defn pf [s & args] (print (apply f s args)))

(defn pfln [s & args] (println (apply f s args)))
