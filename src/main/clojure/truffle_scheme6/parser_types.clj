(ns truffle-scheme6.parser-types
  (:require [clojure.string :as str])
  (:import (java.util.stream IntStream Stream)
           (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.atoms SCharacterLiteralNode SNilLiteralNode SStringLiteralNode SSymbolLiteralNode)
           (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SComplexLiteralNode SExactIntegerNode SExactRealNode SFractionLiteralNode SInexactIntegerNode SInexactReal32Node SInexactReal64Node SOctetLiteralNode)
           (truffle_scheme6.nodes.composites SByteVectorLiteralNode SListNode SVectorLiteralNode)
           (truffle_scheme6.nodes.special SBeginNode SDefineVarNode SIfNode SQuoteNode)))

(defn- node-array
  [aseq]
  (into-array SchemeNode aseq))

(defprotocol PSchemeNode
  (to-java [this] "Transforms a given node to a Java object"))

(extend-protocol PSchemeNode
  nil
  (to-java [this] nil))

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

(defrecord FractionLiteral [numerator-int-literal denominator-int-literal]
  PSchemeNode
  (to-java [this]
    (SFractionLiteralNode. (to-java numerator-int-literal) (to-java denominator-int-literal))))

(defrecord DecimalLiteral [exact? sign decimal-str exp-mark exp-val mantissa-width] ; mantissa is ignored for now
  PSchemeNode
  (to-java [this]
    (let [val (cond exact? (SExactRealNode. ^BigDecimal (BigDecimal. ^String decimal-str))
                    (some #{"s" "S" "f" "F"} [exp-mark]) (SInexactReal32Node. (Float/parseFloat decimal-str))
                    :else (SInexactReal64Node. (Double/parseDouble decimal-str)))
          val (if exp-val (.applyExp val exp-val) val)
          val (if (= sign "-") (.negate val) val)]
      val)))

(defrecord NanInfLiteral [sign literal]
  PSchemeNode
  (to-java [this]
    (let [num (condp = [sign literal]
                ["-" "inf.0"] Float/NEGATIVE_INFINITY
                ["+" "inf.0"] Float/POSITIVE_INFINITY
                Float/NaN)]
      (SInexactReal32Node. num))))

(defrecord ComplexLiteral [real-literal imaginary-literal]
  PSchemeNode
  (to-java [this]
    (SComplexLiteralNode. (to-java real-literal) (to-java imaginary-literal))))

(defrecord OctetLiteral [radix octet-str]
  PSchemeNode
  (to-java [this]
    (let [parsed (Integer/parseUnsignedInt octet-str radix)]
      (if (and (>= parsed 0) (<= parsed 255))
        (SOctetLiteralNode. (unchecked-byte parsed))
        (throw (Exception. "Octet given was not valid. Has to be an unsigned 8-bit integer"))))))

(defrecord CharacterLiteral [utf32value]
  PSchemeNode
  (to-java [this]
    (SCharacterLiteralNode. ^int utf32value)))

(defrecord StringLiteral [utf32codepoints]
  PSchemeNode
  (to-java [this]
    (SStringLiteralNode. (int-array utf32codepoints))))

(defrecord SymbolLiteral [utf32codepoints]
  PSchemeNode
  (to-java [this]
    (SSymbolLiteralNode. (int-array utf32codepoints))))

(defrecord NilLiteral []
  PSchemeNode
  (to-java [this]
    (SNilLiteralNode.)))

(defrecord ListNode [f rs]
  PSchemeNode
  (to-java [this]
    (SListNode. (to-java f) (node-array (map to-java rs)))))

(defrecord VectorNode [xs]
  PSchemeNode
  (to-java [this]
    (SVectorLiteralNode. (node-array (map to-java xs)))))

(defrecord ByteVectorNode [octets]
  PSchemeNode
  (to-java [this]
    (SByteVectorLiteralNode. (into-array SOctetLiteralNode (map to-java octets)))))

(defrecord QuoteNode [quoted-child]
  PSchemeNode
  (to-java [this]
    (SQuoteNode. (to-java quoted-child))))

(defrecord IfNode [condition then else]
  PSchemeNode
  (to-java [this]
    (SIfNode. (to-java condition) (to-java then) (to-java else))))

(defrecord DefVarNode [sym v]
  PSchemeNode
  (to-java [this]
    (SDefineVarNode. (to-java sym) (to-java v))))

(defrecord BeginNode [forms]
  PSchemeNode
  (to-java [this]
    (SBeginNode. (node-array (map to-java forms)))))