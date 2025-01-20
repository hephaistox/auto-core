(ns auto-core.string.regexp
  "Regular expressions


  Regular expression syntax is based on host:
  * [Clojure](https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html)
  * [Clojurescript](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_expressions)

  Have an objective to have a seamless access to regular expressions between cljs and clj"
  #?(:cljs (:require [auto-core.adapters.string :as core-string])))

(def starts-a-string
  "Regular expression for the start of the input string. (the whole input and not the line)"
  #?(:clj "\\A"
     :cljs "^"))

(def ends-a-string
  "Regular expression for the end of the input string. (the whole input and not the line)"
  #?(:clj "\\z"
     :cljs "$"))

(defn stringify
  "Turns `re` - a regular expression object - to a clojure string."
  [re]
  #?(:clj (str re)
     :cljs
       (if (string? re) re (core-string/remove-first-last-character (str re)))))

(defn assemble-re
  "Build one regular expression assembling all elements from `strs-or-res`, that could be:
  * Some strings
  * Some regular expression object

  Returns a regular expression"
  [& strs-or-res]
  (->> (map stringify strs-or-res)
       (apply str)
       re-pattern))

(defn full-sentence-re
  "Returns a regular expression matching `re` exactly"
  [re]
  (->> [starts-a-string (stringify re) ends-a-string]
       (apply str)
       re-pattern))
