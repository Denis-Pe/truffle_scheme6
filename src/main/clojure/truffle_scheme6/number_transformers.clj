(ns truffle-scheme6.number-transformers
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str]
            [instaparse.core :as insta])
  (:import (truffle_scheme6.nodes.atoms.numbers SComplexLiteralNode SExactIntegerNode SFractionLiteralNode SInexactIntegerNode SInexactReal64Node)))

(defn- int-converter-maker [radix]
  (fn [& digits]
    (let [s (apply str digits)
          parsed (Long/parseUnsignedLong s radix)]
      (if (neg? parsed)
        (SExactIntegerNode. s radix)
        (SInexactIntegerNode. parsed)))))

(defn- decimal-converter [content & args]
  (cond (= (first content) ".") (decimal-converter (cons "0" args))
        (= (last content) ".") (decimal-converter (concat args ["0"]))
        :else
        (let [content (butlast args)
              suffix (last args)
              [exp-marker exp-sign & exp-digits] (rest suffix)
              exp-marker (if exp-marker
                           (str/lower-case (second exp-marker))
                           nil)
              num (cond (or (instance? SExactIntegerNode (first content)) (instance? SInexactIntegerNode (first content))) (first content)
                        :else (SInexactReal64Node. (Double/parseDouble (apply str content))))
              main-value (condp = exp-marker
                           "e" (.asReal64 num)
                           "d" (.asReal64 num)
                           "l" (.asReal64 num)
                           "s" (.asReal32 num)
                           "f" (.asReal32 num)
                           (.asReal64 num))]
          (if (empty? (rest suffix))
            main-value
            (let [exp (->> exp-digits (apply str) (str (second exp-sign)) Long/parseLong)]
              (.pow main-value exp))))))

(defn- ureal-converter
  ([uint] uint)
  ([numerator _slash denominator]
   (SFractionLiteralNode. numerator denominator)))

(defn- ureal10-converter
  ([uint] uint)
  ([numerator _slash denominator]
   (SFractionLiteralNode. numerator denominator))
  ; todo do something with the mantissa?
  ([decimal10 _mantissa-width]
   decimal10))

(defn- real-converter
  [sign val]
  (let [sign (if (instance? String sign) sign (second sign))]
    (if (= "-" sign)
      (.negate val)
      val)))

(defn- complex-converter
  ([real] real)
  ([first-elm & args]
   (match [(cons first-elm args)]
     [([a "@" b] :seq)] (SComplexLiteralNode. a b)
     [([a "+" b "i"] :seq)] (SComplexLiteralNode. a b)
     [([a "-" b "i"] :seq)] (SComplexLiteralNode. a (.negate b))
     [([a "+" "i"] :seq)] (SComplexLiteralNode. a (SInexactIntegerNode/one))
     [([a "-" "i"] :seq)] (SComplexLiteralNode. a (.negate (SInexactIntegerNode/one)))
     [(["+" a "i"] :seq)] (SComplexLiteralNode. (SInexactIntegerNode/one) a)
     [(["-" a "i"] :seq)] (SComplexLiteralNode. (SInexactIntegerNode/one) (.negate a))
     [(["+" "i"] :seq)] (SComplexLiteralNode. (SInexactIntegerNode/one) (SInexactIntegerNode/one))
     [(["-" "i"] :seq)] (SComplexLiteralNode. (SInexactIntegerNode/one) (.negate (SInexactIntegerNode/one))))))

(defn- prefix-converter
  [& args] ; always two args, but it's easier to handle as & args
  (if (= (ffirst args) :exactness)
    (first args)
    (second args)))

(defn transform-number
  [prefix content]
  (let [as-ast (list prefix content)
        transformed (insta/transform {:uinteger2  (int-converter-maker 2)
                                      :uinteger8  (int-converter-maker 8)
                                      :uinteger10 (int-converter-maker 10)
                                      :uinteger16 (int-converter-maker 16)

                                      :decimal10  #(apply decimal-converter (cons content %&))

                                      :ureal2     ureal-converter
                                      :ureal8     ureal-converter
                                      :ureal10    ureal10-converter
                                      :ureal16    ureal-converter

                                      :naninf     #(condp = %
                                                     "nan.0" (SInexactReal64Node/nan)
                                                     "inf.0" (SInexactReal64Node/posInf))

                                      :real2      real-converter
                                      :real8      real-converter
                                      :real10     real-converter
                                      :real16     real-converter

                                      :complex2   complex-converter
                                      :complex8   complex-converter
                                      :complex10  complex-converter
                                      :complex16  complex-converter

                                      :prefix2    prefix-converter
                                      :prefix8    prefix-converter
                                      :prefix10   prefix-converter
                                      :prefix16   prefix-converter}

                                     as-ast)
        exactness (second (first transformed))
        number (second transformed)]
    (if exactness
      (let [exactness (str/lower-case exactness)]
        ; todo pass exactness as an argument so no precision is lost,
        ;  as opposed to the current approach in which decimals are parsed as double/float
        ;  then converted to an exact real at this point
        (condp = exactness
          "#i" (.asInexact number)
          "#e" (.asExact number)))
      ; because of how the implementation works, I already get the intended behavior
      ; integers behave as if they're exact because they're promoted automatically
      number)))
