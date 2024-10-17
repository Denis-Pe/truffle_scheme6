(ns truffle-scheme6.number-transformers
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str]
            [instaparse.core :as insta]
            [truffle-scheme6.parser-types :refer :all]))

(defn transform-ureal
  ([exact? radix ureal-node]
   (transform-ureal exact? radix "+" ureal-node))
  ([exact? radix sign ureal-node]
   (let [uint-str (str/join (rest (second ureal-node)))]
     (->IntegerLiteral exact? radix sign uint-str))))

(defn transform-real
  [exact? radix real-node]
  (let [real-node (rest real-node)                          ; skip tag
        sign (first real-node)
        value (vec (rest (second real-node)))]
    ; in naninf, the sign is kind of hardcoded whereas in the rest we have the dedicated sign node
    (match value
      [:naninf "nan.0"] (->NanInfLiteral sign "nan.0")
      [:naninf "inf.0"] (->NanInfLiteral sign "inf.0")
      [uinteger] (->IntegerLiteral exact? radix (second sign) (str/join (rest uinteger)))
      [numerator "/" denominator] (->FractionLiteral (->IntegerLiteral exact? radix (second sign) (str/join (rest numerator)))
                                                     (->IntegerLiteral exact? radix "+" (str/join (rest denominator))))
      [decimal mantissa-width] (let [decimal-str (str/join (butlast (rest decimal)))
                                     suffix (last decimal)
                                     suffix-content (rest suffix)
                                     [exp-mark exp-val] (if (= suffix [:suffix])
                                                          [nil nil]
                                                          [(second (first suffix-content))
                                                           (Integer/parseInt (str
                                                                               ; the sign of the exponent
                                                                               (second (second suffix-content))
                                                                               ; the digits of the exponent
                                                                               (str/join (drop 2 suffix-content))))])]
                                 (->DecimalLiteral exact? sign decimal-str exp-mark exp-val (str/join (rest mantissa-width)))))))

(defn transform-complex
  [exact? radix & args]
  (match (vec args)
    [real] (transform-real exact? radix real)
    [real "@" real-imaginary] (->ComplexLiteral
                                (transform-real exact? radix real)
                                (transform-real exact? radix real-imaginary))
    [real "+" [:naninf naninf-lit] "i"] (->ComplexLiteral
                                          (transform-real exact? radix real)
                                          (->NanInfLiteral "+" naninf-lit))
    [real "-" [:naninf naninf-lit] "i"] (->ComplexLiteral
                                          (transform-real exact? radix real)
                                          (->NanInfLiteral "-" naninf-lit))
    [real "+" ureal-imaginary "i"] (->ComplexLiteral
                                     (transform-real exact? radix real)
                                     (transform-ureal exact? radix ureal-imaginary))
    [real "-" ureal-imaginary "i"] (->ComplexLiteral
                                     (transform-real exact? radix real)
                                     (transform-ureal exact? radix "-" ureal-imaginary))
    [real "+" "i"] (->ComplexLiteral
                     (transform-real exact? radix real)
                     (->IntegerLiteral exact? radix "+" "1"))
    [real "-" "i"] (->ComplexLiteral
                     (transform-real exact? radix real)
                     (->IntegerLiteral exact? radix "-" "1"))
    ["+" [:naninf naninf-lit] "i"] (->ComplexLiteral
                                     (->IntegerLiteral exact? radix "+" 0)
                                     (->NanInfLiteral "+" naninf-lit))
    ["-" [:naninf naninf-lit] "i"] (->ComplexLiteral
                                     (->IntegerLiteral exact? radix "+" 0)
                                     (->NanInfLiteral "-" naninf-lit))
    ["+" ureal-imag "i"] (->ComplexLiteral
                           (->IntegerLiteral exact? radix "+" "0")
                           (transform-ureal exact? radix ureal-imag))
    ["-" ureal-imag "i"] (->ComplexLiteral
                           (->IntegerLiteral exact? radix "+" "0")
                           (transform-ureal exact? radix "-" ureal-imag))
    ["+" "i"] (->ComplexLiteral
                (->IntegerLiteral exact? radix "+" "0")
                (->IntegerLiteral exact? radix "+" "1"))
    ["-" "i"] (->ComplexLiteral
                (->IntegerLiteral exact? radix "+" "0")
                (->IntegerLiteral exact? radix "-" "1"))))

(defn transform-number
  [prefix content]
  (let [prefix (rest prefix)                                ; skip tag
        exactness (if (= (ffirst prefix) :exactness) (second (first prefix)) (second (second prefix)))
        exact? (and (some? exactness) (= (str/lower-case exactness) "#e"))
        transformed (insta/transform {:complex2  (partial transform-complex exact? 2)
                                      :complex8  (partial transform-complex exact? 8)
                                      :complex10 (partial transform-complex exact? 10)
                                      :complex16 (partial transform-complex exact? 16)}
                                     content)]
    transformed))
