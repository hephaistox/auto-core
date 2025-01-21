(ns auto-core.log.fe-log
  "Factory generating log function"
  (:require
   [auto-core.log.fe-registry]
   [auto-core.log.static-ns-level]
   [auto-core.log.strategy])
  #?(:cljs (:require-macros [auto-core.log.fe-log])))

(defn- logger-ids-to-logger-fns
  [logger-ids]
  (reduce (fn [acc logger-id]
            (conj acc
                  (get-in #?(:cljs auto-core.log.fe-registry/strategies-registry
                             :clj {})
                          [logger-id :impl])))
          []
          logger-ids))

(defmacro log
  [logger-id level & message]
  (let [ns (str *ns*)
        log-fns `(auto-core.log.fe-log/logger-ids-to-logger-fns ~logger-id)]
    `((apply juxt ~log-fns) ~ns ~level ~@message)))

(defmacro log-exception
  [logger-id level exception & additional-message]
  (when additional-message `(log ~logger-id ~level ~@additional-message))
  (let [ns (str *ns*)
        log-fns `(auto-core.log.fe-log/logger-ids-to-logger-fns ~logger-id)]
    `((apply juxt ~log-fns) ~ns ~level ~exception)))

(defmacro log-data
  [logger-id level data & additional-message]
  (when additional-message `(log ~logger-id ~level ~@additional-message))
  (let [ns (str *ns*)
        log-fns `(auto-core.log.fe-log/logger-ids-to-logger-fns ~logger-id)]
    `((apply juxt ~log-fns) ~ns ~level ~data)))

(defmacro log-format
  [logger-id level fmt & data]
  (let [ns (str *ns*)
        log-fns `(auto-core.log.fe-log/logger-ids-to-logger-fns ~logger-id)]
    `((apply juxt ~log-fns) ~ns ~level (format ~fmt ~@data))))
