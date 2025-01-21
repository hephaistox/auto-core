(ns auto-core.data.sequence-test
  (:require
   [auto-core.data.sequence :as sut]
   #?(:clj [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test :refer [deftest is testing] :include-macros true])))

(def v ["a" "b" "c"])

(deftest idx-of-test
  (testing "Basic case"
    (is (= 0 (sut/idx-of v "a")))
    (is (= 1 (sut/idx-of v "b")))
    (is (= 2 (sut/idx-of v "c"))))
  (testing "not found values return nil" (is (nil? (sut/idx-of v "z"))))
  (testing "nil values are ok" (is (nil? (sut/idx-of v nil))) (is (nil? (sut/idx-of nil "v")))))

(def v2 [{:foo :bar} {:foo :bar2}])

(deftest idx-of-pred-test
  (testing "Basic case" (is (= 0 (sut/idx-of-pred v2 #(= :bar (:foo %))))))
  (testing "not found values return nil"
    (is (nil? (sut/idx-of-pred v #(= :not-existing (:foo %))))))
  (testing "nil values are ok"
    (is (nil? (sut/idx-of-pred v nil)))
    (is (nil? (sut/idx-of-pred nil "v")))))

(deftest trim-leading-nil-test
  (testing "basic example"
    (is (= ["a" "b"] (sut/trim-leading-nil ["a" "b"])))
    (is (= ["a" "b"] (sut/trim-leading-nil ["a" "b" nil])))
    (is (= ["a" "b"] (sut/trim-leading-nil ["a" "b" nil nil nil nil])))
    (is (= [nil "a" nil nil "b"] (sut/trim-leading-nil [nil "a" nil nil "b" nil nil nil nil]))))
  (testing "edge cases"
    (is (empty? (sut/trim-leading-nil [nil nil nil nil nil])))
    (is (empty? (sut/trim-leading-nil [])))
    (is (empty? (sut/trim-leading-nil nil)))))

(deftest index-of-test
  (testing "Element found in the sequence"
    (is (= 2 (sut/index-of [1 2 :foo 3] #{:foo})))
    (is (= 0 (sut/index-of [:foo 1 2 3] #{:foo})))
    (is (= 3 (sut/index-of [1 2 3 :foo] #{:foo}))))
  (testing "Element not found in the sequence" (is (nil? (sut/index-of [1 2 3] :foo)))))
