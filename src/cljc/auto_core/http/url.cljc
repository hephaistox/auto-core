(ns auto-core.http.url
  "Parse url

  See [lambdaisland.uri](https://cljdoc.org/d/lambdaisland/uri/1.15.125/doc/readme) for useful functions
  And see the [RFC](https://www.ietf.org/rfc/rfc3986.txt) for references"
  (:require
   [lambdaisland.uri :as lambda-uri]))

(comment
  (into {} (lambda-uri/uri "http://www.hephaistox.com/foo'bar?lang=fr"))
  ;{:scheme "http",
  ; :user nil, :password nil, :host "www.hephaistox.com", :port nil, :path
  ; "/foo'bar", :query "lang=fr",
  ; :fragment nil}
)

(def url-delims
  "According to [RFC3986 page 12](https://www.ietf.org/rfc/rfc3986.txt):
   gen-delims  = : / ? #  [ ] @
   sub-delims  = ! $ & ' ( ) * + , ; ="
  {:gen-delims [":" "/" "?" "#" "[" "]" "@"]
   :sub-delims ["!" "$" "&" "'" "(" ")" "*" "+" "," ";" "="]})

(defn parse
  "Destructuring the `url` into this map:
  * `user`
  * `password`
  * `host`
  * `port`
  * `path`
  * `query`
  * `fragment`"
  [url]
  (lambda-uri/uri url))

(defn parse-queries
  "Parse queries to get the parameters from `url`"
  [parsed-url]
  (-> parsed-url
      :query
      lambda-uri/query-string->map))

(defn extract-tld
  "Extract the tld from the `url`

  The tld is the top level domain, which is `com` in `hephaistox.com`"
  [parsed-url]
  (some->> parsed-url
           :host
           (re-find #".*(?:\.([a-zA-Z]\w{1,2}))(?::\d{1,4})?$")
           second))
