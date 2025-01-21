(ns auto-core.log.be-log-test
  (:require [auto-core.log :as core-log]
            [auto-core.log.be-log :as sut]
            [auto-core.log.be-registry]
            [auto-core.log.log4j2]
            [auto-core.log.registry]
            [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]))

(deftest log-test
  (testing
    "Log expand to text-based log4j2. Meaning the strategy applied at compile time, and as expected"
    (is (= (macroexpand `(core-log/trace "Hey"))
           (macroexpand
             `(sut/log [:auto-core.log.registry/no-op] :trace "Hey"))))
    (is (= `((juxt ~auto-core.log.be-registry/no-op-fn) ~*ns* :trace "Hey" "Ho")
           (macroexpand
             `(sut/log [:auto-core.log.registry/no-op] :trace "Hey" "Ho")))))
  (testing
    "Fatal expand to print. Meaning the strategy applied at compile time, and as expected"
    (is (= `((juxt ~auto-core.log.log4j2/log-fn) ~*ns* :fatal "Hey")
           (macroexpand `(core-log/fatal "Hey")))))
  (testing "Fatal is happening and printing the expected string"
    (is (= "Hey\n"
           (with-out-str
             (sut/log [:auto-core.log.registry/print] :fatal "Hey")))))
  (testing "Trace is happening and printing the expected string"
    (is (= "Hey\n"
           (with-out-str
             (sut/log [:auto-core.log.registry/print] :trace "Hey"))))))

(deftest fatal-exception-test
  (testing "Printed strategy applies to exception when fatal level is caught\n"
    (is (str/starts-with? (with-out-str (sut/log-exception
                                          [:auto-core.log.registry/print]
                                          :fatal
                                          (ex-info "msg" {:data :foo})))
                          "#error {\n :cause msg"))))

(deftest fatal-format-test
  (testing "Expansion"
    (is (= `((juxt ~auto-core.log.be-registry/simple-print)
              ~*ns*
              :fatal
              (format "Hello %s and %s" "mati" "anthony"))
           (macroexpand `(sut/log-format [:auto-core.log.registry/print]
                                         :fatal "Hello %s and %s"
                                         "mati" "anthony")))))
  (testing "Printed strategy applies to exception when fatal level is caught"
    (is (= "Hello mati and anthony\n"
           (with-out-str (sut/log-format [:auto-core.log.registry/print]
                                         :fatal "Hello %s and %s"
                                         "mati" "anthony"))))))
