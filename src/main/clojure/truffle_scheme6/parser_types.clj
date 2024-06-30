(ns truffle-scheme6.parser-types
  (:require [clojure.string :as str])
  (:import (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SExactIntegerNode SExactRealNode SFractionLiteralNode SInexactIntegerNode SInexactReal32Node SInexactReal64Node)))

(defprotocol PSchemeNode
  (to-java [this] "Transforms a given node to a Java object"))

(defrecord FalseLiteral []
  PSchemeNode
  (to-java [this]
    (SFalseLiteralNode.)))

(defrecord TrueLiteral []
  PSchemeNode
  (to-java [this]
    (STrueLiteralNode.)))

(defrecord IntegerLiteral [exact? radix sign uint-str]
  PSchemeNode
  (to-java [this]
    (let [val (if exact?
                (SExactIntegerNode. (BigInteger. ^String uint-str ^int radix))
                (SInexactIntegerNode. (Long/parseLong uint-str radix)))]
      (if (= sign "-")
        (.negate val)
        val))))

(defrecord FractionLiteral [exact? radix sign numerator-str denominator-str]
  PSchemeNode
  (to-java [this]
    (let [numerator (->IntegerLiteral exact? radix sign numerator-str)
          denominator (->IntegerLiteral exact? radix "+" denominator-str)]
      (SFractionLiteralNode. (to-java numerator) (to-java denominator)))))

(defrecord DecimalLiteral [exact? sign decimal-str exp-mark exp-val mantissa-width] ; mantissa is ignored for now
  PSchemeNode
  (to-java [this]
    (let [val (cond exact? (SExactRealNode. ^BigDecimal (BigDecimal. ^String decimal-str))
                    (some #{"s" "S" "f" "F"} [exp-mark]) (SInexactReal32Node. (Float/parseFloat decimal-str))
                    :else (SInexactReal64Node. (Double/parseDouble decimal-str)))
          val (if exp-val (.pow val exp-val) val)
          val (if (= sign "-") (.negate val) val)]
      val)))