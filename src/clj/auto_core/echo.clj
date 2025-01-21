(ns auto-core.echo
  (:require
   [auto-core.echo.actions :as build-echo-actions]
   [auto-core.echo.headers :as build-echo-headers]))

(def action-printers build-echo-actions/printers)

(def level1-header
  (let [{:keys [h1 h1-valid h1-error h2 h2-error normalln errorln uri-str expectionln print-cmd]}
        build-echo-headers/printers]
    {:title h1
     :title-valid h1-valid
     :title-error h1-error
     :subtitle h2
     :subtitle-error h2-error
     :normalln normalln
     :errorln errorln
     :print-cmd print-cmd
     :exceptionln expectionln
     :uri-str uri-str}))
