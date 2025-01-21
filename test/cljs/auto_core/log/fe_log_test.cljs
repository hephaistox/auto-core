(ns auto-core.log.fe-log-test
  (:require
   [auto-core.log.fe-log :as sut]
   [cljs.test            :refer          [deftest is]
                         :include-macros true]))

(deftest log-fatal-test
  (is (= "foo bar" (with-out-str (sut/log [:auto-core.log.registry/print] :fatal "foo" "bar")))
      "Fatal is always printing something")
  (is (= '((clojure.core/apply
            clojure.core/juxt
            (auto-core.log.fe-log/logger-ids-to-logger-fns [:auto-core.log.registry/print]))
           "auto-core.log.fe-log-test"
           :fatal
           "foo"
           "bar")
         (macroexpand '(sut/log [:auto-core.log.registry/print] :fatal "foo" "bar")))
      "Macroexpansion is resolving until the key of the chosen method"))

(comment
  (macroexpand '(sut/log-exception [:auto-core.log.registry/print] :trace (ex-info "foo" {}) "bar"))
  ;((clojure.core/apply
  ;  clojure.core/juxt
  ;  (auto-core.log.fe-log/logger-ids-to-logger-fns
  ;   [:auto-core.log.registry/print]))
  ; "auto-core.log.fe-log-test" :trace
  ; (ex-info "foo" {}))
)

(deftest trace-test
  (is (= "" (with-out-str (sut/log [:auto-core.log.registry/no-op] :trace "foo" "bar")))
      "Trace is not accepted in log namespace test rule"))
