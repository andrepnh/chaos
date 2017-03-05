(ns maps.pages.template
  (:require [bidi.bidi :as bidi]
            [maps.pages.fragment :refer [fragment]]
            [maps.routes :refer [routes]]
            [reagent.session :as session]))

(defn template []
  (fn []
    (let [route (:current-page (session/get :route))]
      [:div
       [:p [:a {:href (bidi/path-for routes :index)} "Home"]]
       [:hr]
       ^{:key route} [fragment route]
       [:hr]])))