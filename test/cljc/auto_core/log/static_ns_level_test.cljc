(ns auto-core.log.static-ns-level-test
  (:require #?(:clj [clojure.test :refer [deftest is testing]]
               :cljs [cljs.test :refer [deftest is testing] :include-macros
                      true])
            [auto-core.log.registry :as log-registry]
            [auto-core.log.strategy :as log-strategy]
            [auto-core.log.static-ns-level :as sut]))

(deftest ns-rules-test
  (let [ns-chooser-stub (sut/make-static-ns-level-strategy sut/ns-rules)]
    (is
      (every? (set (keys log-registry/strategies-registry))
              (log-strategy/rule-ids ns-chooser-stub))
      "Check all used strategies are known strategies in `auto-core.log.strategy/registry`")))

(def ns-chooser-stub (sut/make-static-ns-level-strategy sut/ns-rules))
(def ns-rules-map-stub
  (into {}
        (map (fn [{:keys [rule-id], :as rule}] [rule-id rule])
          (:ns-rules ns-chooser-stub))))
(def get-stubbed-rule (fn [rule-id] (rule-id ns-rules-map-stub)))

(deftest apply-ns-rule-test
  (let [ns-chooser-stub (sut/make-static-ns-level-strategy sut/ns-rules)
        ns-rules-map-stub (into {}
                                (map (fn [{:keys [rule-id], :as rule}] [rule-id
                                                                        rule])
                                  (:ns-rules ns-chooser-stub)))
        get-stubbed-rule (fn [rule-id] (rule-id ns-rules-map-stub))]
    (testing "ns outside the scope are rejected"
      (is (nil? (sut/apply-ns-rule "other ns" (get-stubbed-rule :default))))
      (is (nil? (sut/apply-ns-rule "auto-core"
                                   (get-stubbed-rule :ac-edn-utils)))))
    (testing "ns matching the scope are accepted"
      (is (map? (sut/apply-ns-rule "auto-core.adapters.edn-utils"
                                   (get-stubbed-rule :ac-edn-utils))))
      (is (map? (sut/apply-ns-rule "auto-web.duplex.message-handler"
                                   (get-stubbed-rule :aw-duplex-messages)))))))

(deftest choose-logger-test
  (let [ns-chooser-stub (sut/make-static-ns-level-strategy sut/ns-rules)
        apply-strategy* (partial log-strategy/apply-strategy ns-chooser-stub)]
    (is (= [:auto-core.log.registry/text-based]
           (apply-strategy* "auto-core" :fatal))
        "One matching is returning the expected match")
    (is (= [:auto-core.log.registry/text-based]
           (apply-strategy* "auto-core.adapters.edn-utils" :fatal))
        "Among many matches the first one is returned")
    (is (= [:auto-core.log.registry/text-based]
           (apply-strategy* "non-existing-namespace" :fatal))
        "Default logger is text-based")))
