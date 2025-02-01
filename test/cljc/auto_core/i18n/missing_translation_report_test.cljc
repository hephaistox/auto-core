(ns auto-core.i18n.missing-translation-report-test
  (:require
   #?(:clj [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test :refer [deftest is testing] :include-macros true])
   [auto-core.i18n.missing-translation-report :as sut]))

(def dic-sample
  "Dictionary sample"
  {:en {:bar "anthony"
        :nested "hey"
        :en-only "yep"}
   :esperanto {:bar "mati"
               :esp-only "yeah"}})

(deftest language-report
  (testing "List keys and their set languages"
    (is (= {:bar #{:esperanto :en}
            :nested #{:en}
            :en-only #{:en}
            :esp-only #{:esperanto}}
           (sut/language-report dic-sample #{:en :esperanto}))))
  (testing "Language report contains only expected ones"
    (is (= {:bar #{:en}
            :nested #{:en}
            :en-only #{:en}}
           (sut/language-report dic-sample #{:en})))))

(deftest key-with-missing-languages
  (testing "Return keys with missing languages"
    (is (= [[:esp-only #{:esperanto}] [:nested #{:en}] [:en-only #{:en}]]
           (sut/key-with-missing-languages dic-sample #{:en :esperanto} []))))
  (testing "Keys could be excluded"
    (is (= [[:esp-only #{:esperanto}] [:nested #{:en}] [:en-only #{:en}]]
           (sut/key-with-missing-languages dic-sample #{:en :esperanto} [:nested.one])))
    (is (= [[:esp-only #{:esperanto}] [:nested #{:en}]]
           (sut/key-with-missing-languages dic-sample #{:en :esperanto} [:nested.one :en-only])))
    (is (= [[:esp-only #{:esperanto}]]
           (sut/key-with-missing-languages dic-sample #{:en :esperanto} [:nested :en-only])))))
