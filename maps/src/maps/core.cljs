(ns maps.core
  (:require [reagent.core :as reagent]))

(enable-console-print!)

(defonce points (reagent/atom []))

(defonce ticks (reagent/atom 0))

(defonce x-min (reagent/atom 0))

(defonce x-max (reagent/atom 150))

(defonce y-min (reagent/atom 0))

(defonce y-max (reagent/atom 1))

(defonce width (reagent/atom 800))

(defonce height (reagent/atom 600))

(defonce speed (reagent/atom 1700))

(def options
  {:chart       {:type "line"}
   :title       {:text ""}
   :xAxis       {:gridLineWidth 1
                 :tickWidth     0
                 :title         {:text ""}
                 :tickInterval  1
                 :plotLines     [{:color     "black"
                                  :dashStyle "solid"
                                  :width     1
                                  :value     0}]}
   :yAxis       {:title        {:text ""}
                 :tickInterval 1
                 :plotLines    [{:color     "black"
                                 :dashStyle "solid"
                                 :width     1
                                 :valueh    0}]
                 }
   :plotOptions {:line {:animation false
                        :marker    {:enabled   true
                                    :fillColor "red"
                                    :symbol    "circle"
                                    :radius    1}}}
   :series      [{:showInLegend  false
                  :softThreshold false}]
   :credits     {:enleinabled false}})

(defn chart-config []
  (-> options
      (assoc-in [:xAxis :min] @x-min)
      (assoc-in [:xAxis :max] @x-max)
      (assoc-in [:yAxis :min] @y-min)
      (assoc-in [:yAxis :max] @y-max)
      (assoc-in [:series 0 :data] @points)))

(defn chart-render [wd ht ps]
  @ps
  [:div {:style {:min-width "310px"
                 :max-width (str @wd "px")
                 :height    (str @ht "px")
                 :margin    "0 auto"}}])

(defn series-updated [this]
  (js/Highcharts.Chart. (reagent/dom-node this)
                        (clj->js (chart-config))))

(defn chart [wd ht ps]
  (reagent/create-class {:reagent-render       #(chart-render wd ht ps)
                         :component-did-update #(series-updated %1)}))

(defn slider [id v min max]
  [:input {:id        id
           :type      "range"
           :min       min
           :max       max
           :value     @v
           :on-change #(reset! v (-> % .-target .-value))}])

(defn logistic-map [x]
  (* 3.9341 x (- 1 x)))

(defn evolution! [seed ps sp]
  (letfn [(evol [x y]
            (do (swap! ps conj [x y])
                (js/setTimeout #(evol (inc x) (logistic-map y)) (- 2200 @sp))))]
    (evol 0 seed)))

(defn page []
  [:div
   [:div
    [:label {:for "x-min"} "x-axis min value"]
    [slider "x-min" x-min -500 0]]
   [:div
    [:label {:for "x-max"} "x-axis max value"]
    [slider "x-max" x-max 1 1000]]
   [:div
    [:label {:for "y-min"} "y-axis min value"]
    [slider "y-min" y-min -500 0]]
   [:div
    [:label {:for "y-max"} "y-axis max value"]
    [slider "y-max" y-max 1 500]]
   [:div
    [:label {:for "width"} "width"]
    [slider "width" width 400 3000]]
   [:div
    [:label {:for "height"} "height"]
    [slider "y-max" height 400 1080]]
   [:div
    [:label {:for "speed"} "speed"]
    [slider "speed" speed 200 2000]]
   [chart width height points]])

(reagent/render-component [page]
                          (. js/document (getElementById "app")))

(defonce foo (evolution! 0.851 points, speed))