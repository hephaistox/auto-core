(ns auto-core.configuration
  (:require
   [auto-core.os.edn-utils :as core-edn]))

(def filename "run_config.edn")

(defn load-config ([printers filename] (core-edn/read-edn printers filename)))

(def configuration-filedesc
  "File descriptor - should be checked by the application when starting"
  (load-config nil filename))

(defn print-if-error
  [{:keys [errorln uri-str]
    :as _printers}]
  (errorln "Configuration file" (uri-str (:afilepath configuration-filedesc)) "is not well setup"))

(def configuration (:edn configuration-filedesc))
