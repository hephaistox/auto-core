(ns compile
  (:require
   [clojure.tools.build.api :refer [compile-clj create-basis jar]]))

(defn compile-jar
  [{:keys []
    :as _args}]
  (let [class-dir "target/classes"
        basis (create-basis)]
    (compile-clj {:basis basis
                  :bindings {#'clojure.core/*assert* false
                             #'clojure.core/*warn-on-reflection* true}
                  :class-dir class-dir})
    (jar {:class-dir class-dir
          :jar-file "target/production/auto_core.jar"})))
