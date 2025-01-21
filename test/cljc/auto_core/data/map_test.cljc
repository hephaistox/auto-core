(ns auto-core.data.map-test
  (:require
   [auto-core.data.map :as sut]
   #?(:clj [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test :refer [deftest is testing] :include-macros true])))

(deftest deep-merge-test
  (is (= {:one 1
          :two {}}
         (sut/deep-merge {:one 1
                          :two 2}
                         {:one 1
                          :two {}}))
      "last map has higher priority")
  (is (= {:one 1
          :two {:three {:test true}
                :four {:five 5}}}
         (sut/deep-merge {:one 1
                          :two {:three 3
                                :four {:five 5}}}
                         {:two {:three {:test true}}}))
      "Two level of nesting")
  (is (= {:one {:two {:three "three"
                      :nine 9}
                :seven 7}
          :four {:five 5
                 :eight 8}
          :ten 10}
         (sut/deep-merge {:one {:two {:three 3}}
                          :four {:five {:six 6}}}
                         {:one {:seven 7
                                :two {:three "three"
                                      :nine 9}}
                          :four {:eight 8
                                 :five 5}
                          :ten 10}))
      "Two level of nesting")
  (is (= {:one {:two 2
                :three 3
                :four 4
                :five 5}}
         (sut/deep-merge {:one {:two 2
                                :three 3}}
                         {:one {:four 4
                                :five 5}}))
      "Non conflicting keys are merged")
  (is (= {:one 1
          :two {:three 3}}
         (sut/deep-merge {:one 1
                          :two {:three 3}}
                         nil))
      "Nil is working as an empty map")
  (is (= {:one 1
          :two {:three 3
                :fourth 4
                :fifth 5}}
         (sut/deep-merge {:one 1
                          :two {:three 3}}
                         nil
                         {:one 1
                          :two {:fourth 4}}
                         nil
                         nil
                         {:one 1
                          :two {:fifth 5}}))
      (is (= {:one 4
              :two {:three 6}}
             (sut/deep-merge {:one 1
                              :two {:three 3}}
                             {:one 2
                              :two {:three 4}}
                             {:one 3
                              :two {:three 5}}
                             {:one 4
                              :two {:three 6}}))
          "Multiple maps are merged, last one is higher priority")))


(deftest add-ids-test
  (is (= {:foo {:bar "bar"
                :id :foo}
          :bar {:foo "foo"
                :id :bar}}
         (sut/add-ids {:foo {:bar "bar"}
                       :bar {:foo "foo"}}))
      "Simple maps")
  (is (= {} (sut/add-ids {})) "Empty maps are ok"))

;; ********************************************************************************
;; Various
;; ********************************************************************************

(deftest remove-nil-vals-test
  (testing "Remove nil values"
    (is (= {} (sut/remove-nil-vals nil)))
    (is (= {} (sut/remove-nil-vals {})))
    (is (= {} (sut/remove-nil-vals {:foo nil})))
    (is (= {:barfoo :foobar}
           (sut/remove-nil-vals {:foo nil
                                 :bar nil
                                 :barfoo :foobar})))))

(deftest map-difference-test
  (testing "The same maps are returning no difference"
    (is (= {} (sut/map-difference {} {})))
    (is (= {} (sut/map-difference {:a 1} {:a 1})))
    (is (= {}
           (sut/map-difference {:a 1
                                :d 6
                                :b {:c 2
                                    :d 6}}
                               {:a 1
                                :b {:c 2
                                    :d 6}
                                :d 6})))
    (is (= {}
           (sut/map-difference {:a 2
                                :b 5}
                               {:b 5
                                :a 2}))))
  (testing "Difference is found"
    (is (= {:a 2} (sut/map-difference {:a 2} {})))
    (is (= {:a 2} (sut/map-difference {} {:a 2})))
    (is (= {:b 2}
           (sut/map-difference {:a 1}
                               {:a 1
                                :b 2})))
    (is (= {:b {:c 2
                :d 6}}
           (sut/map-difference {:a 1
                                :d 6
                                :b {:c 2
                                    :d 6}}
                               {:a 1
                                :b {:c 2
                                    :d 5}
                                :d 6})))
    (is (= {:a 2}
           (sut/map-difference {:a 2
                                :b 5}
                               {:b 5
                                :a 3})))))

;; ********************************************************************************
;; When keys are integers
;; ********************************************************************************

(deftest get-key-or-before-test
  (testing "Test if latest key is returned, even if doesn't exist"
    (is (nil? (sut/get-key-or-before (into (sorted-map)
                                           {1 :a
                                            32 :b
                                            2 :c})
                                     0)))
    (is (= 1
           (sut/get-key-or-before (into (sorted-map)
                                        {1 :a
                                         32 :b
                                         2 :c})
                                  1)))
    (is (= 1
           (sut/get-key-or-before (into (sorted-map)
                                        {1 :a
                                         32 :b
                                         2 :c})
                                  1.3)))
    (is (= 2
           (sut/get-key-or-before (into (sorted-map)
                                        {1 :a
                                         32 :b
                                         2 :c})
                                  2)))
    (is (= 2
           (sut/get-key-or-before (into (sorted-map)
                                        {1 :a
                                         32 :b
                                         2 :c})
                                  30)))
    (is (= 32
           (sut/get-key-or-before (into (sorted-map)
                                        {1 :a
                                         32 :b
                                         2 :c})
                                  32)))
    (is (nil? (sut/get-key-or-before (into (sorted-map) {}) 32)))))
