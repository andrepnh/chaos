(ns maps.components
  (:require [reagent.core :as reagent]))

(defn slider [id v min max]
  [:span [:input {:id        id
                  :type      "range"
                  :min       min
                  :max       max
                  :value     @v
                  :on-change #(reset! v (-> % .-target .-value))}]
   @v])

(defn bound-sliders
  [lower-id lower-v lower-min upper-id upper-v upper-max]
  [:div
   [:div
    [:label {:for lower-id} (str lower-id " value")]
    [:input {:id        lower-id
             :type      "range"
             :min       lower-min
             :max       (dec (int @upper-v))
             :value     @lower-v
             :on-change #(reset! lower-v (-> % .-target .-value))}]
    @lower-v]
   [:div
    [:label {:for upper-id} (str upper-id " value")]
    [:input {:id        upper-id
             :type      "range"
             :min       (inc (int @lower-v))
             :max       upper-max
             :value     @upper-v
             :on-change #(reset! upper-v (-> % .-target .-value))}]
    @upper-v]])

(def ^:private highcharts-options
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

(defn- highcharts-config
  [{x-min :x-min x-max :x-max y-min :y-min y-max :y-max}
   {data :data scale :scale skip :skip}]
  (-> highcharts-options
      (assoc-in [:xAxis :min] @x-min)
      (assoc-in [:xAxis :max] @x-max)
      (assoc-in [:yAxis :min] @y-min)
      (assoc-in [:yAxis :max] @y-max)
      (assoc-in [:series 0 :data]
                (take-nth (int @scale) (drop (int @skip) @data)))))

(defn- chart-render [wd ht ps]
  @ps
  [:div {:style {:min-width "310px"
                 :max-width (str @wd "px")
                 :height    (str @ht "px")
                 :margin    "0 auto"}}])

(defn- series-updated [this chart-config dataset]
  (js/Highcharts.Chart. (reagent/dom-node this)
                        (clj->js (highcharts-config chart-config dataset))))

(defn chart
  [{width :width height :height :as chart-config}
   {data :data :as dataset}]
  (reagent/create-class {:reagent-render       #(chart-render width height data)
                         :component-did-update #(series-updated %1 chart-config dataset)}))