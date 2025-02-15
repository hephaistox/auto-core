(ns auto-core.keyword-test
  (:require
   [auto-core.keyword :as sut]
   #?(:clj [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test :refer [deftest is testing] :include-macros true])))

(deftest keywordize-test
  (testing "Basic case" (is (= :hello (sut/keywordize :hello))))
  (testing "capital letters turned into lower case" (is (= :hello (sut/keywordize :HeLLo))))
  (testing "_ turned into -" (is (= :hello-world (sut/keywordize :hello_world))))
  (testing ". turned into -" (is (= :hello-world (sut/keywordize :hello-world))))
  (testing "Works also on strings" (is (= :hello-my-world (sut/keywordize "hello.MY_wOrLd")))))

(deftest sanitize-map-keys-test
  (testing "Basic case"
    (is (= {:hello "world"
            :my-amazing :liFe}
           (sut/sanitize-map-keys {:heLLo "world"
                                   :my_amazing :liFe}))))
  (testing "Works on nested maps"
    (is (= {:hello "world"
            :my-amazing :liFe
            :three {:two-more {:another-one 1
                               :normal-one :true}
                    :one "hi"}}
           (sut/sanitize-map-keys {:Hello "world"
                                   :my_amazing :liFe
                                   :three {:two_more {:another.one 1
                                                      :normal-one :true}
                                           :ONE "hi"}})))))


(deftest unkeywordize-test
  (testing "Are keywords turned into string"
    (is (= "foo" (sut/unkeywordize :foo)))
    (is (= "foo" (sut/unkeywordize :bar/foo))))
  (testing "Are other types returned as-is"
    (is (= 1 (sut/unkeywordize 1)))
    (is (= [] (sut/unkeywordize [])))
    (is (= "bar" (sut/unkeywordize "bar")))))
