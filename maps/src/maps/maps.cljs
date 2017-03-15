(ns maps.maps)

(defonce active-maps (atom 0))

(defn logistic-map [x]
  (* 3.9341 x (- 1 x)))

(defn tent-map [x]
  (if (< x 0.5) (* 1.1337 x) (* 1.1337 (- 1 x))))

(defn evolution! [points interval f seed active-maps]
  (letfn [(evol [[x y :as point]]
            (when (> @active-maps 0)
              (swap! points conj point)
              (js/setTimeout #(evol [(inc x) (f y)]) @interval)))]
    (if (empty? @points)
      (evol [0 seed])
      (evol (last @points)))))