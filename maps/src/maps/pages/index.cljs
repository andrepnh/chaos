(ns maps.pages.index
  (:require [bidi.bidi :as bidi]
            [maps.pages.fragment :refer [fragment]]
            [maps.routes :refer [routes]]))

(defmethod fragment :index []
  [:ul
   [:li [:a {:href (bidi/path-for routes :logistic-map)} "Logistic map"]]])