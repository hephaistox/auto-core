(ns auto-core.env
  "Environment variable"
  #?(:clj (:require
           [auto-core.java-properties :refer [get-java-property]])))

(def env
  #?(:clj (get-java-property "env" "dev")
     :cljs "dev"))
