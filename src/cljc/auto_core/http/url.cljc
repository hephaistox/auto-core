(ns auto-core.http.url
  "Url management

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

(defn extract-tld-from-host
  "Extract the tld of an host from an `url`"
  [url]
  (some->> url
           (re-find #".*(?:\.([a-zA-Z]\w{1,2}))(?::\d{1,4})?$")
           second))

(def url-delims
  "According to [RFC3986 page 12](https://www.ietf.org/rfc/rfc3986.txt):
   gen-delims  = : / ? #  [ ] @
   sub-delims  = ! $ & ' ( ) * + , ; ="
  {:gen-delims [":" "/" "?" "#" "[" "]" "@"]
   :sub-delims ["!" "$" "&" "'" "(" ")" "*" "+" "," ";" "="]})

(defn compare-locations
  "Are `url` from the same location? (apparent server)"
  [& urls]
  (apply = (map (comp (juxt :path :query) lambda-uri/uri) urls)))

(defn parse-queries
  "Parse queries to get the parameters from `url`"
  [url]
  (-> url
      lambda-uri/uri
      :query
      lambda-uri/query-string->map))
