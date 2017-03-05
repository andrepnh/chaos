(ns maps.core
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]
            [reagent.core :as r]
            [reagent.session :as session]
            [maps.pages.template :refer [template]]
            [maps.pages.index]
            [maps.pages.logistic-map]
            [maps.routes :refer [routes]]))

(enable-console-print!)

(defn on-js-reload []
  (r/render-component [template]
                      (. js/document (getElementById "app"))))

(defn ^:export init! []
  (accountant/configure-navigation!
    {:nav-handler  (fn
                     [path]
                     (let [match (bidi/match-route routes path)
                           current-page (:handler match)
                           route-params (:route-params match)]
                       (session/put! :route {:current-page current-page
                                             :route-params route-params})))
     :path-exists? (fn [path]
                     (boolean (bidi/match-route routes path)))})
  (accountant/dispatch-current!)
  (on-js-reload))