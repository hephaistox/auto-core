(ns auto-core.http.url-test
  (:require
   [auto-core.http.url :as sut]
   #?@(:clj [[clojure.test :refer [deftest is testing]]]
       :cljs [[cljs.test :refer [deftest is testing] :include-macros true]])))

(deftest parse-test
  (is (= {:scheme "http"
          :user nil
          :password nil
          :host "www.auto-core.com"
          :port nil
          :path "/foo'bar"
          :query "lang=fr"
          :fragment nil}
         (into {} (sut/parse "http://www.auto-core.com/foo'bar?lang=fr"))))
  (is (= {:scheme "http"
          :user nil
          :password nil
          :host "www.auto-core.com"
          :port nil
          :path nil
          :query nil
          :fragment nil}
         (into {} (sut/parse "http://www.auto-core.com")))))

(deftest parse-queries-test
  (is (= {:par "foo"
          :bar "barfoo"}
         (-> "http://auto-core.com?par=foo&bar=barfoo"
             sut/parse
             sut/parse-queries))
      "Simple params")
  (is (= {:par ""}
         (-> "?par="
             sut/parse
             sut/parse-queries))
      "Emtpy param")
  (testing "No params"
    (is (nil? (-> "?"
                  sut/parse
                  sut/parse-queries)))
    (is (nil? (-> ""
                  sut/parse
                  sut/parse-queries)))
    (is (nil? (-> nil
                  sut/parse
                  sut/parse-queries))))
  (is (= {:par "foo"
          :bar "barfoo"}
         (-> "http://auto-core.com:3000?par=foo&bar=barfoo#foobar"
             sut/parse
             sut/parse-queries))
      "Complete url analysis"))

(deftest extract-tld-test
  (testing "Find existing tld"
    (is (= "com"
           (-> "http://auto-core.com"
               sut/parse
               sut/extract-tld)))
    (is (= "fr"
           (-> "http://auto-core.fr"
               sut/parse
               sut/extract-tld)))
    (is (= "fr"
           (-> "http://auto-core.fr"
               sut/parse
               sut/extract-tld))))
  (testing "Compatible with ports"
    (is (nil? (-> "localhost:3000"
                  sut/parse
                  sut/extract-tld)))
    (is (= "com"
           (-> "http://auto-core.com:3000"
               sut/parse
               sut/extract-tld)))
    (is (= "uk"
           (-> "https://auto-core.co.uk"
               sut/parse
               sut/extract-tld)))
    (is (= "fr"
           (-> "http://auto-core.fr"
               sut/parse
               sut/extract-tld))))
  (testing "Compatible with multiple domaines"
    (is (= "fr"
           (-> "http://www.subdomain.auto-core.fr"
               sut/parse
               sut/extract-tld))))
  (testing "Localhosts have no tld"
    (is (nil? (-> "localhost"
                  sut/parse
                  sut/extract-tld)))
    (is (nil? (-> "192.168.0.01"
                  sut/parse
                  sut/extract-tld)))))
