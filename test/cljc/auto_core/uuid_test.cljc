(ns auto-core.uuid-test
  (:require
   [auto-core.uuid :as sut]
   [clojure.test   :refer [deftest is]]))

(deftest unguessable
  (is (every? uuid? (repeatedly 10 #(sut/unguessable))) "check that generates proper uuid"))
