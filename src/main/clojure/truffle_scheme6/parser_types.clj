(ns truffle-scheme6.parser-types
  (:require [clojure.core.match :refer [match]])
  (:import (com.oracle.truffle.api.frame FrameDescriptor FrameSlotKind)
           (org.graalvm.collections Pair)
           (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.atoms SCharacterLiteralNode SNilLiteralNode SStringLiteralNode SSymbolLiteralNode SSymbolLiteralNode$ReadArgDispatch SSymbolLiteralNode$ReadGlobal SSymbolLiteralNodeFactory SSymbolLiteralNodeFactory$ReadLocalNodeGen)
           (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SComplexLiteralNode SExactIntegerNode SExactRealNode SFractionLiteralNode SInexactIntegerNode SInexactReal32Node SInexactReal64Node SOctetLiteralNode)
           (truffle_scheme6.nodes.composites SByteVectorLiteralNode SListNode SVectorLiteralNode)
           (truffle_scheme6.nodes.functions SReadArgSlotNode SReadVarArgsNode)
           (truffle_scheme6.nodes.special SBeginNode SDefineVarNode SLetNode SQuoteNode)))

(defn- node-array
  [aseq]
  (into-array SchemeNode aseq))

(defprotocol PSchemeNode
  ; organized in the order that they would be run from a top-level parser
  (specialize [this] "Transforms a list node into an appropriate special node, otherwise returns the same thing")
  (tagged [this symbol-codepoints->dispatch frame-desc-builder] "Transform child symbols according to specified dispatch. Should be run on child nodes")
  (to-java [this] "Transforms a given node to a Java object"))

(defn create-global-dispatch []
  :global)

(defn create-local-dispatch [int-key]
  [:local int-key])

(defn create-arg-dispatch [arg-name position rest-arg?]
  [:argument position
   :rest-arg? rest-arg?])

(defn dispatch->java-obj [dispatch]
  (match dispatch
    :global (SSymbolLiteralNode$ReadGlobal.)
    [:local k] (SSymbolLiteralNodeFactory$ReadLocalNodeGen/create k)
    [:argument pos
     :rest-arg? rest-arg?] (if rest-arg?
                             (SSymbolLiteralNode$ReadArgDispatch. (SReadVarArgsNode. pos)) ; after pos, which is also the # of args preceding it
                             (SSymbolLiteralNode$ReadArgDispatch. (SReadArgSlotNode. pos)))))

(defrecord FalseLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
  (to-java [this]
    (SFalseLiteralNode.)))

(defrecord TrueLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
  (to-java [this]
    (STrueLiteralNode.)))

(defrecord IntegerLiteral [exact? radix sign uint-str]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
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
  (tagged [this _ _] this)
  (to-java [this]
    (SFractionLiteralNode. (to-java numerator-int-literal) (to-java denominator-int-literal))))

(defrecord DecimalLiteral [exact? sign decimal-str exp-mark exp-val mantissa-width] ; mantissa is ignored for now
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
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
  (tagged [this _ _] this)
  (to-java [this]
    (let [num (condp = [sign literal]
                ["-" "inf.0"] Float/NEGATIVE_INFINITY
                ["+" "inf.0"] Float/POSITIVE_INFINITY
                Float/NaN)]
      (SInexactReal32Node. num))))

(defrecord ComplexLiteral [real-literal imaginary-literal]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
  (to-java [this]
    (SComplexLiteralNode. (to-java real-literal) (to-java imaginary-literal))))

(defrecord OctetLiteral [radix octet-str]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
  (to-java [this]
    (let [parsed (Integer/parseUnsignedInt octet-str radix)]
      (if (and (>= parsed 0) (<= parsed 255))
        (SOctetLiteralNode. (unchecked-byte parsed))
        (throw (Exception. "Octet given was not valid. Must be an unsigned 8-bit integer"))))))

(defrecord CharacterLiteral [utf32value]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
  (to-java [this]
    (SCharacterLiteralNode. ^int utf32value)))

(defrecord StringLiteral [utf32codepoints]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
  (to-java [this]
    (SStringLiteralNode. (int-array utf32codepoints))))

(defrecord SymbolLiteral [utf32codepoints read-var-dispatch]
  PSchemeNode
  (specialize [this] this)
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (->SymbolLiteral
      utf32codepoints
      (if-let [d (symbol-codepoints->dispatch utf32codepoints)]
        d
        read-var-dispatch)))
  (to-java [this]
    (SSymbolLiteralNode. (int-array utf32codepoints)
                         (dispatch->java-obj read-var-dispatch))))

(defrecord NilLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)
  (to-java [this]
    (SNilLiteralNode.)))

;; special nodes

(defrecord QuoteNode [x]
  PSchemeNode
  (specialize [this] this)
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (->QuoteNode (tagged x symbol-codepoints->dispatch frame-desc-builder)))
  (to-java [this]
    (SQuoteNode. (to-java x))))

(defrecord DefineNode [identifier value]
  PSchemeNode
  (specialize [this] (->DefineNode identifier (specialize value)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (->DefineNode (tagged identifier symbol-codepoints->dispatch frame-desc-builder)
                  (tagged value symbol-codepoints->dispatch frame-desc-builder)))
  (to-java [this]
    (SDefineVarNode. (to-java identifier) (to-java value))))

(defrecord BeginNode [nodes]
  PSchemeNode
  (specialize [this] (->BeginNode (map specialize nodes)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (->BeginNode (map #(tagged % symbol-codepoints->dispatch frame-desc-builder)
                      nodes)))
  (to-java [this]
    (SBeginNode. (node-array (map to-java nodes)))))

(defrecord LetNode [bindings body-forms]
  PSchemeNode
  (specialize [this]
    (->LetNode
      (mapv (fn [[sym val]] [sym (specialize val)]) bindings)
      (map specialize body-forms)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (let [sc->new-dispatch
          (->> bindings
               (map (fn [[sym _value]]                      ;; todo detect literals from _value and set the kind of the frame slot
                      [(:utf32codepoints sym) (create-local-dispatch
                                                (.addSlot frame-desc-builder, FrameSlotKind/Illegal, nil, nil))]))
               (reduce
                 (fn [m [codepoints dispatch]]
                   (assoc m codepoints dispatch))
                 symbol-codepoints->dispatch))]
      (->LetNode
        (mapv (fn [[sym val]] [(tagged sym sc->new-dispatch frame-desc-builder)
                               (tagged val symbol-codepoints->dispatch frame-desc-builder)])
              bindings)
        (map #(tagged % sc->new-dispatch frame-desc-builder)
             body-forms))))
  (to-java [this]
    (SLetNode.
      (into-array
        Pair
        (map (fn [[sym val]] (Pair/create (to-java sym)
                                          (to-java val)))
             bindings))
      (node-array (map to-java body-forms)))))

(defmulti specialize-list
  "Return a special form node if the args given match
  the pattern of a syntax, otherwise returns nil"
  (fn [special-syntax-name & rest-nodes]
    special-syntax-name))

(defrecord ListNode [forms dotted]
  PSchemeNode
  (specialize [this]
    (let [[f & rs] forms]
      (if (instance? SymbolLiteral f)
        (let [cps (int-array (:utf32codepoints f))
              as-str (String. cps 0 (count cps))]
          (if-let [spec-form (apply specialize-list as-str rs)]
            (specialize spec-form)                          ; specialize children nodes too
            (->ListNode (map specialize forms) dotted)))
        (->ListNode (map specialize forms) dotted))))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (->ListNode (map #(tagged % symbol-codepoints->dispatch frame-desc-builder)
                     forms)
                dotted))
  (to-java [this]
    (let [[f & rs] forms
          rs (if dotted rs (concat rs [(->NilLiteral)]))
          f (to-java f)
          rs (to-java rs)]
      (SListNode. f (node-array rs)))))

(defrecord VectorNode [xs]
  PSchemeNode
  (specialize [this] (->VectorNode (map specialize xs)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (->VectorNode (map #(tagged % symbol-codepoints->dispatch frame-desc-builder)
                       xs)))
  (to-java [this]
    (SVectorLiteralNode. (node-array (map to-java xs)))))

(defrecord ByteVectorNode [octets]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _] this)                                  ;; can't have anything other than octets at the parser level
  (to-java [this]
    (SByteVectorLiteralNode. (into-array SOctetLiteralNode (map to-java octets)))))

(defmethod specialize-list "quote" [_quote & args]
  (if (= 1 (count args))
    (->QuoteNode (first args))))

(defmethod specialize-list "define" [_define & args]
  ; todo implement rest of the Variations on the Carnival of Definitions by J.B. Arban
  (if (and (= 2 (count args))
           (instance? SymbolLiteral (first args)))
    (let [[identifier value] args]
      (->DefineNode identifier value))))

(defmethod specialize-list "begin" [_begin & args]
  (->BeginNode args))

(defmethod specialize-list "let" [_let & args]
  (if (and (>= (count args) 2)
           (instance? ListNode (first args))
           (every? (fn [binding]
                     (and (instance? ListNode binding)
                          (= 2 (count (:forms binding)))
                          (instance? SymbolLiteral (first (:forms binding)))))
                   (:forms (first args))))
    (let [bindings-node (first args)
          bindings (mapv (comp vec :forms) (:forms bindings-node))
          body (rest args)
          binding-names (map first bindings)]
      (if (= (count (set binding-names))
             (count binding-names))
        (->LetNode bindings body)))))

(defmethod specialize-list :default [& _] nil)