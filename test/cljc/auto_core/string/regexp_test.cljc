(ns auto-core.string.regexp-test
  (:require
   [auto-core.string.regexp :as sut]
   #?(:clj [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test :refer [deftest is testing] :include-macros true])))

(deftest assemble-re-test
  (testing "Test assembly of strings" (is (= "abc" (sut/stringify (sut/assemble-re "a" "b" "c")))))
  (testing "Test assembly of re" (is (= "abc" (sut/stringify (sut/assemble-re #"a" #"b" #"c")))))
  (testing "Test assembly of mixed" (is (= "abc" (sut/stringify (sut/assemble-re #"a" "b" #"c"))))))

(deftest full-sentence-re-test
  (testing "Check terminators are working for clj and cljs"
    (let [res (sut/stringify (sut/full-sentence-re #"foo"))]
      (is (or (= res "\\Afoo\\z") (= res "^foo$"))))))
