(ns moriarty1
  (:require [clojure.string :as string]
            [clojure.math :as math]))

(def namespace-name "Moriarty1")

;; problem-sets are defined by tag and name
(def tag-names
  {::all "All"})

(defn fuzzy-equals [a b]
  (< (Math/abs (- a b)) 0.001))

(def problems
  [{:name- "quantum state analyzer"
    :args ["integer" "integer" "integer"]
    :function (fn [a b c]
                (let [sum (+ a b c)
                      product (* a b c)
                      remainder (mod product 7)
                      divisibility (cond
                                     (zero? (mod sum 5)) 2
                                     (zero? (mod sum 3)) 3
                                     (zero? (mod sum 2)) 5
                                     :else 1)]
                  (if (pos? product)
                    (+ remainder (* divisibility 10))
                    (* -1 (+ remainder (* divisibility 10))))))
    :verifications [[1 2 3], [5 0 5], [2 2 2], [-1 3 4], [10 5 2]]
    :output-type "integer"
    :test-limit 30
    :tags #{::all}}
   
   {:name- "planetary zones"
    :args ["integer" "integer"]
    :function (fn [x y]
                (let [distance (Math/sqrt (+ (* x x) (* y y)))
                      angle (if (and (zero? x) (zero? y)) 
                              0 
                              (Math/toDegrees (Math/atan2 y x)))
                      normalized-angle (mod (+ angle 360) 360)
                      quadrant (cond 
                                 (< normalized-angle 90) 1
                                 (< normalized-angle 180) 2
                                 (< normalized-angle 270) 3
                                 :else 4)
                      zone-distance (cond
                                      (< distance 5) 1
                                      (< distance 10) 2
                                      (< distance 15) 3
                                      :else 4)
                      element (case quadrant
                                1 (case zone-distance
                                    1 "mercury"
                                    2 "venus"
                                    3 "earth"
                                    "mars")
                                2 (case zone-distance
                                    1 "jupiter"
                                    2 "saturn"
                                    3 "uranus"
                                    "neptune")
                                3 (case zone-distance
                                    1 "fire"
                                    2 "water"
                                    3 "earth"
                                    "air")
                                4 (case zone-distance
                                    1 "spring"
                                    2 "summer"
                                    3 "autumn"
                                    "winter"))]
                  element))
    :verifications [[0 0], [3 4], [6 8], [12 5], [-8 -6], [-5 12], [4 -3]]
    :output-type "string"
    :test-limit 40
    :tags #{::all}}
   
   {:name- "elemental phase analyzer"
    :args ["integer" "string"]
    :function (fn [temp scale]
                (let [celsius (case scale
                                "C" temp
                                "F" (int (/ (- temp 32) 1.8))
                                "K" (- temp 273)
                                "R" (int (/ (- temp 492) 1.8))
                                "Invalid scale - must be C, F, K, or R")]
                  (if (string? celsius)
                    celsius
                    (let [material (string/lower-case scale)
                          boiling-points {"c" 100  ; water
                                         "f" 357   ; mercury
                                         "k" 2792  ; gold
                                         "r" 883}  ; nitrogen
                          freezing-points {"c" 0
                                          "f" -39
                                          "k" 1064
                                          "r" -210}
                          boiling (get boiling-points material)
                          freezing (get freezing-points material)]
                      (cond
                        (nil? boiling) "Unknown material"
                        (< celsius freezing) "Solid"
                        (> celsius boiling) "Gas"
                        :else "Liquid")))))
    :verifications [[100 "C"], [32 "F"], [1100 "K"], [-20 "C"], [400 "F"], [15 "R"]]
    :output-type "string"
    :test-limit 40
    :tags #{::all}}
   
   {:name- "multi-layered cipher system"
    :args ["string" "integer"]
    :function (fn [input key]
                (let [shifted-caesar (fn [c shift]
                                       (if (Character/isLetter c)
                                         (let [base (if (Character/isUpperCase c) \A \a)
                                               offset (mod (+ (- (int c) (int base)) shift) 26)]
                                           (char (+ (int base) offset)))
                                         c))
                      reverse-segment (fn [s] (apply str (reverse s)))
                      key-val (mod (Math/abs key) 26)
                      seed (mod (* key-val 17) 5)
                      segments (partition-all (+ 2 seed) input)
                      processed (map-indexed
                                 (fn [idx segment]
                                   (let [segment-str (apply str segment)
                                         reversed? (zero? (mod (+ idx seed) 2))
                                         segment-processed (if reversed?
                                                            (reverse-segment segment-str)
                                                            segment-str)
                                         shift-val (mod (+ key-val idx) 26)]
                                     (apply str (map #(shifted-caesar % shift-val) segment-processed))))
                                 segments)]
                  (apply str processed)))
    :verifications [["hello" 3], ["abcde" 1], ["TEST" 12], ["scientific" 7], ["apple" 0]]
    :output-type "string"
    :test-limit 40
    :tags #{::all}}
  ])
