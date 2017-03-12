(ns maps.pages.logistic-map
  (:require [maps.components :as comps]
            [maps.maps :as maps]
            [maps.pages.fragment :refer [fragment]]
            [reagent.core :as reagent]))

(defonce points (reagent/atom []))

(defonce x-min (reagent/atom 0))

(defonce x-max (reagent/atom 150))

(defonce y-min (reagent/atom 0))

(defonce y-max (reagent/atom 1))

(defonce width (reagent/atom 800))

(defonce height (reagent/atom 600))

(defonce pause (reagent/atom 1100))

(defonce skip (reagent/atom 0))

(defonce scale (reagent/atom 1))

(defmethod fragment :logistic-map []
  [:div
   [comps/bound-sliders "x-min" x-min -500 "x-max" x-max 2000]
   [:div
    [:label {:for "y-min"} "y-axis min value"]
    [comps/slider "y-min" y-min -500 0]]
   [:div
    [:label {:for "y-max"} "y-axis max value"]
    [comps/slider "y-max" y-max 1 500]]
   [:div
    [:label {:for "width"} "width"]
    [comps/slider "width" width 400 3000]]
   [:div
    [:label {:for "height"} "height"]
    [comps/slider "y-max" height 400 1080]]
   [:div
    [:label {:for "pause"} "pause"]
    [comps/slider "pause" pause 200 2000]]
   [:div
    [:label {:for "skip"} "skip"]
    [comps/slider "skip" skip 0 1000]]
   [:div
    [:label {:for "scale"} "scale"]
    [comps/slider "scale" scale 1 10]]
   [comps/chart
    {:width width :height height
     :x-min x-min :x-max x-max
     :y-min y-min :y-max y-max}
    {:data points :scale scale :skip skip}]
   [(reagent/create-class
      {:reagent-render         #(vector :div)
       :component-did-mount    #(do (compare-and-set! maps/active-maps 0 1)
                                    (maps/evolution! points pause maps/logistic-map 0.851 maps/active-maps))
       :component-will-unmount #(swap! maps/active-maps dec)})]])



