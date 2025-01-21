(ns auto-core.log.log4j2
  "It's a simple redirection to clojure tools logging
   Set to log4j2 [check setup in](clojure/deps.edn)

   For more information read docs/tutorial/logging.md"
  (:require [clojure.tools.logging :as l]
            [auto-core.string :as core-string]
            [clojure.pprint :as pp]))

(defn- one-liner-print
  "Prepare the element `elt` to display in the print
           Params:
            * `elt` data to show, which type will be checked"
  [elt]
  (if (or (map? elt) (set? elt) (vector? elt))
    (-> elt
        pp/pprint
        with-out-str
        core-string/remove-last-character)
    elt))

(defn log-fn
  [ns level & message]
  (l/log ns level nil (apply str "" (map one-liner-print message))))
