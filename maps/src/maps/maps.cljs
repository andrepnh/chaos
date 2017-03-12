(ns maps.maps)

(defonce active-maps (atom 0))

(defn logistic-map [n]
  (* 3.9341 n (- 1 n)))

(defn evolution! [points interval f seed active-maps]
  (letfn [(evol [[x y :as point]]
            (when (> @active-maps 0)
              (swap! points conj point)
              (js/setTimeout #(evol [(inc x) (f y)]) @interval)))]
    (if (empty? @points)
      (evol [0 seed])
      (evol (last @points)))))