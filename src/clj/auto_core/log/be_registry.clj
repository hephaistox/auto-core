(ns auto-core.log.be-registry
  "List of all known backend strategies"
  (:require [auto-core.log.log4j2]
            [auto-core.log.registry :as log-registry]))

(defn no-op-fn [& _] nil)

(defn simple-print [_ns _level & msg] (apply println msg))

(def strategies-registry
  "List of predefined strategies
  Look at clj and cljs implementations to have an understanding of that strategies implementation which may be different on both technologies.
  For instance, `:text-based` will be based on log4j2 on backedn and console on frontend"
  {::log-registry/print {:description "Simple print", :impl simple-print},
   ::log-registry/text-based
     {:description
        "Basic one - sends everything to flatten text (js/console for clojurescript and log4j2 for clojure)",
      :impl auto-core.log.log4j2/log-fn},
   ::log-registry/no-op {:description "Deactivate that log", :impl no-op-fn}})
