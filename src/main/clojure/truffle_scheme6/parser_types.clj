(ns truffle-scheme6.parser-types
  (:require [clojure.core.match :refer [match]])
  (:import (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.atoms SCharacterLiteralNode SNilLiteralNode SStringLiteralNode SSymbolLiteralNode SSymbolLiteralNode$ReadGlobal)
           (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SComplexLiteralNode SExactIntegerNode SExactRealNode SFractionLiteralNode SInexactIntegerNode SInexactReal32Node SInexactReal64Node SOctetLiteralNode)
           (truffle_scheme6.nodes.composites SByteVectorLiteralNode SListNode SVectorLiteralNode)
           (truffle_scheme6.nodes.special SBeginNode SDefineVarNode SQuoteNode)))

(defn- node-array
  [aseq]
  (into-array SchemeNode aseq))

(defprotocol PSchemeNode
  ; organized in the order that they would be run from a top-level parser
  (specialize [this] "Transforms a list node into an appropriate special node, otherwise returns the same thing")
  (tagged [this symbol-literal->dispatch] "Transform children symbols according to scoping rules. Collections should run this on children nodes")
  (to-java [this] "Transforms a given node to a Java object"))

(defrecord FalseLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (SFalseLiteralNode.)))

(defrecord TrueLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (STrueLiteralNode.)))

(defrecord IntegerLiteral [exact? radix sign uint-str]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (let [val (if exact?
                (SExactIntegerNode. (BigInteger. ^String uint-str ^int radix))
                (SInexactIntegerNode. (Long/parseLong uint-str radix)))]
      (if (= sign "-")
        (.negate val)
        val))))

(defrecord FractionLiteral [numerator-int-literal denominator-int-literal]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (SFractionLiteralNode. (to-java numerator-int-literal) (to-java denominator-int-literal))))

(defrecord DecimalLiteral [exact? sign decimal-str exp-mark exp-val mantissa-width] ; mantissa is ignored for now
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (let [val (cond exact? (SExactRealNode. ^BigDecimal (BigDecimal. ^String decimal-str))
                    (some #{"s" "S" "f" "F"} [exp-mark]) (SInexactReal32Node. (Float/parseFloat decimal-str))
                    :else (SInexactReal64Node. (Double/parseDouble decimal-str)))
          val (if exp-val (.applyExp val exp-val) val)
          val (if (= sign "-") (.negate val) val)]
      val)))

(defrecord NanInfLiteral [sign literal]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (let [num (condp = [sign literal]
                ["-" "inf.0"] Float/NEGATIVE_INFINITY
                ["+" "inf.0"] Float/POSITIVE_INFINITY
                Float/NaN)]
      (SInexactReal32Node. num))))

(defrecord ComplexLiteral [real-literal imaginary-literal]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (SComplexLiteralNode. (to-java real-literal) (to-java imaginary-literal))))

(defrecord OctetLiteral [radix octet-str]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (let [parsed (Integer/parseUnsignedInt octet-str radix)]
      (if (and (>= parsed 0) (<= parsed 255))
        (SOctetLiteralNode. (unchecked-byte parsed))
        (throw (Exception. "Octet given was not valid. Must be an unsigned 8-bit integer"))))))

(defrecord CharacterLiteral [utf32value]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (SCharacterLiteralNode. ^int utf32value)))

(defrecord StringLiteral [utf32codepoints]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (SStringLiteralNode. (int-array utf32codepoints))))

; todo tag the dispatch
(defrecord SymbolLiteral [utf32codepoints read-var-dispatch]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (SSymbolLiteralNode. (int-array utf32codepoints)
                         read-var-dispatch)))

(defrecord NilLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _] this)
  (to-java [this]
    (SNilLiteralNode.)))

;; special nodes

(defrecord QuoteNode [x]
  PSchemeNode
  (specialize [this] this)
  (tagged [this symbol-literal->dispatch]
    (->QuoteNode (tagged x symbol-literal->dispatch)))
  (to-java [this]
    (SQuoteNode. (to-java x))))

(defrecord DefineNode [identifier value]
  PSchemeNode
  (specialize [this] (->DefineNode identifier (specialize value)))
  (tagged [this symbol-literal->dispatch]
    (->DefineNode (tagged identifier symbol-literal->dispatch)
                  (tagged value symbol-literal->dispatch)))
  (to-java [this]
    (SDefineVarNode. (to-java identifier) (to-java value))))

(defrecord BeginNode [nodes]
  PSchemeNode
  (specialize [this] (->BeginNode (map specialize nodes)))
  (tagged [this symbol-literal->dispatch]
    (->BeginNode (map #(tagged % symbol-literal->dispatch) nodes)))
  (to-java [this]
    (SBeginNode. (node-array (map to-java nodes)))))

(defmulti specialize-list
  "Return a special form node if the args given match
  the pattern of a syntax, otherwise returns nil"
  (fn [special-syntax-name & rest-nodes]
    special-syntax-name))

(defmethod specialize-list "quote" [_quote & args]
  (if (= 1 (count args))
    (->QuoteNode (first args))))

(defmethod specialize-list "define" [_define & args]
  (if (and (= 2 (count args))
           (instance? SymbolLiteral (first args)))
    (let [[identifier value] args]
      (->DefineNode identifier value))))

(defmethod specialize-list "begin" [_begin & args]
  (->BeginNode args))

(defrecord ListNode [f rs]
  PSchemeNode
  (specialize [this]
    (if (instance? SymbolLiteral f)
      (let [clean-rs (butlast rs)                           ; drop nil
            cps (int-array (:utf32codepoints f))
            as-str (String. cps 0 (count cps))]
        (if-let [spec-form (apply specialize-list as-str clean-rs)]
          (specialize spec-form)
          this))
      (->ListNode (specialize f)
                  (map specialize rs))))
  (tagged [this symbol-literal->dispatch]
    (->ListNode (tagged f symbol-literal->dispatch)
                (map #(tagged % symbol-literal->dispatch) rs)))
  (to-java [this]
    (SListNode. (to-java f) (node-array (map to-java rs)))))

(defrecord VectorNode [xs]
  PSchemeNode
  (specialize [this] (->VectorNode (map specialize xs)))
  (tagged [this symbol-literal->dispatch]
    (->VectorNode (map #(tagged % symbol-literal->dispatch) xs)))
  (to-java [this]
    (SVectorLiteralNode. (node-array (map to-java xs)))))

(defrecord ByteVectorNode [octets]
  PSchemeNode
  (specialize [this] this)
  (tagged [this symbol-literal->dispatch] this)             ;; can't have anything other than octets at the parser level
  (to-java [this]
    (SByteVectorLiteralNode. (into-array SOctetLiteralNode (map to-java octets)))))