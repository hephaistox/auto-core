(ns auto-core.data.fixed-size-queue-test
  (:require [auto-core.data.fixed-size-queue :as sut]
            [clojure.test :refer [deftest is]]))

(deftest fixed-size-queue-test
  (is (= [:c :b :a]
         (-> (sut/init 3)
             (sut/enqueue :a)
             (sut/enqueue :b)
             (sut/enqueue :c)
             sut/content)))
  (is (= [:d :c :b]
         (-> (sut/init 3)
             (sut/enqueue :a)
             (sut/enqueue :b)
             (sut/enqueue :c)
             (sut/enqueue :d)
             sut/content)))
  (is (= [:c :b]
         (-> (sut/init 3)
             (sut/enqueue :a)
             (sut/enqueue :b)
             sut/pop
             (sut/enqueue :c)
             sut/content))))
