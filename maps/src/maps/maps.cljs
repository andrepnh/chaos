(ns maps.maps)

(defn logistic-map [n]
  (* 3.9341 n (- 1 n)))

(defn evolution! [points interval f seed]
  (letfn [(evol [x y]
            (do (swap! points conj [x y])
                (js/setTimeout #(evol (inc x) (f y)) @interval)))]
    (evol 0 seed)))