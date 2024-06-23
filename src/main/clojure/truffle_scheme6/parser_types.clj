(ns truffle-scheme6.parser-types
  (:require [clojure.string :as str])
  (:import (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SExactIntegerNode SExactRealNode SFractionLiteralNode SInexactIntegerNode SInexactReal32Node SInexactReal64Node)))

(defn- parse-float
  [s]
  (Float/parseFloat s))

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

(defrecord IntegerLiteral [exact? sign uint-str]
  PSchemeNode
  (to-java [this]
    (let [val (if exact?
                (SExactIntegerNode. (BigInteger. ^String uint-str))
                (SInexactIntegerNode. (parse-long uint-str)))]
      (if (= sign "-")
        (.negate val)
        val))))

(defrecord FractionLiteral [exact? sign numerator-str denominator-str]
  PSchemeNode
  (to-java [this]
    (let [numerator (->IntegerLiteral exact? sign numerator-str)
          denominator (->IntegerLiteral exact? "+" denominator-str)]
      (SFractionLiteralNode. (to-java numerator) (to-java denominator)))))