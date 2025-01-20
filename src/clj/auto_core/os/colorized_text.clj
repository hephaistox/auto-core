(ns auto-core.os.colorized-text
  "Colorize text. The reference comes from:
  [ANSI reference sheet](https://gist.github.com/ConnerWill/d4b6c776b509add763e17f9f113fd25b)
  and full reference is [vt100 spec](https://vt100.net/docs/vt100-ug/chapter3.html).")

(def bg-black "\u001b[40m")
(def bg-red "\u001b[41m")
(def bg-green "\u001b[42m")
(def bg-yellow "\u001b[43m")
(def bg-blue "\u001b[44m")
(def bg-magenta "\u001b[45m")
(def bg-cyan "\u001b[46m")
(def bg-gray "\u001b[47m")

(def style-reset-all "\u001b[0m")

(def clear-eol "\u001b[0K")
(def clear-bol "\u001b[1K")
(def clear-line "\u001b[2K")

(def style-bold "\u001b[1m")
(def style-dimfaint "\u001b[2m")
(def style-italic "\u001b[3m")
(def style-underline "\u001b[4m")
(def style-blinking "\u001b[5m")
(def style-reversed "\u001b[7m")
(def style-hidden "\u001b[8m")
(def style-strikethrough "\u001b[9m")

(def style-nobold "\u001b[22m")
(def style-noitalicbold "\u001b[23m")
(def style-nounderline "\u001b[24m")
(def style-noblinking "\u001b[25m")
(def style-noinverse "\u001b[27m")
(def style-novisible "\u001b[28m")
(def style-nostrikethrough "\u001b[29m")

(def font-black "\u001b[30m")
(def font-default "\u001b[39m")
(def font-red "\u001b[31m")
(def font-green "\u001b[32m")
(def font-yellow "\u001b[33m")
(def font-magenta "\u001b[35m")
(def font-cyan "\u001b[36m")
(def font-white "\u001b[37m")

(def move-home "\u001b[H")
(def move-oneup "\u001b[1A")
(defn move-up [n] (str "\u001b[" n "A"))
(defn move-down [n] (str "\u001b[" n "B"))
(defn move-right [n] (str "\u001b[" n "C"))
(defn move-left [n] (str "\u001b[" n "D"))
(defn move-to [line column] (str "\u001b[" line ";" column "]"))
(defn move-bol-down [nb-lines-down] (str "\0001b[" nb-lines-down "E"))
(defn move-bol-up [nb-lines-up] (str "\0001b[" nb-lines-up "F"))
(defn move-column [col] (str "\0001b[" col "G"))
(def move-oneup-with-scroll "\u001bM")

(defn underline [text] (str style-underline text style-nounderline))

(defn line-with-bg
  [text color]
  (str style-reset-all color text clear-eol style-reset-all))
