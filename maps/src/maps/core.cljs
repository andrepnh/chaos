(ns maps.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defonce points (reagent/atom []))

(defn chart-config [points]
  {:chart       {:type "line"}
   :title       {:text ""}
   :xAxis       {:gridLineWidth 1
                 :tickWidth     0
                 :title         {:text ""}
                 :min           0
                 :max           150
                 :tickInterval  1
                 :plotLines     [{:color     "black"
                                  :dashStyle "solid"
                                  :width     1
                                  :value     0}]}
   :yAxis       {:title        {:text ""}
                 :min          0
                 :max          1
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
   :series      [{:showInLegend false :softThreshold false :data points}]
   :credits     {:enleinabled false}})

(defn chart-render [points]
  (fn []
    @points                                                 ; deref to force update
    [:div {:style {:min-width "310px"
                   :max-width "800px"
                   :height    "600px"
                   :margin    "0 auto"}}]))

(defn series-updated [this series]
  (js/Highcharts.Chart. (reagent/dom-node this) (clj->js (chart-config series))))

(defn chart [points]
  (reagent/create-class {:reagent-render       #(chart-render points)
                         :component-did-update #(series-updated %1 @points)}))

(defn logistic-map [x]
  (* 3.9341 x (- 1 x)))

(defn evolution! [_ seed]
  (let [x (atom seed)
        ticks (atom 0)]
    (fn [points _]
      (let [x' (logistic-map @x)
            ticks' (swap! ticks inc)]
        (swap! points conj [ticks' x'])
        (js/setTimeout #(reset! x x') 1000)
        [:div [chart points] (str [ticks' x'])]))))

(defn page []
  [:div [evolution! points 0.851]])

(reagent/render-component [page]
                          (. js/document (getElementById "app")))