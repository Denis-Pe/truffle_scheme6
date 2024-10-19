(ns truffle-scheme6.parser-types
  (:require [clojure.core.match :refer [match]])
  (:import (com.oracle.truffle.api.frame FrameDescriptor FrameSlotKind)
           (org.graalvm.collections Pair)
           (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.atoms SCharacterLiteralNode SNilLiteralNode SStringLiteralNode SSymbolLiteralNode SSymbolLiteralNode$ReadArgDispatch SSymbolLiteralNode$ReadGlobal SSymbolLiteralNodeFactory$ReadLocalNodeGen)
           (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SComplexLiteralNode SExactNumberNode SFractionLiteralNode SInexactIntegerNode SInexactReal32Node SInexactReal64Node SOctetLiteralNode)
           (truffle_scheme6.nodes.composites SByteVectorLiteralNode SListNode SVectorLiteralNode)
           (truffle_scheme6.nodes.functions SReadArgSlotNode SReadVarArgsNode)
           (truffle_scheme6.nodes.special SBeginNode SDefineVarNode SLambdaNode SLetNode SQuoteNode)))

(defn- node-array
  [aseq]
  (into-array SchemeNode aseq))

(defn all-unique?
  [sequential]
  (reduce
    (fn [acc next]
      (if (contains? acc next)
        (reduced false)
        (conj acc next)))
    #{}
    sequential))

(defprotocol PSchemeNode
  ; organized in the order that they would be run from a top-level parser
  (specialize [this] "Transforms a list node into an appropriate special node, otherwise returns the same thing")
  (tagged [this symbol-codepoints->dispatch frame-desc-builder] "Transform child symbols according to specified dispatch. Should be run on child nodes")
  (to-java [this] "Transforms a given node to a Java object"))

(defn except-unsupported [node op-name]
  (UnsupportedOperationException.
    (str op-name " not supported on node " node " of type " (type node))))

(defrecord GlobalDispatch []
  PSchemeNode
  (specialize [this] (throw (except-unsupported this "PSchemeNode/specialize")))
  (tagged [this _ _] (throw (except-unsupported this "PSchemeNode/tagged")))
  (to-java [this] (SSymbolLiteralNode$ReadGlobal.)))

(defrecord LocalDispatch [int-key]
  PSchemeNode
  (specialize [this] (throw (except-unsupported this "PSchemeNode/specialize")))
  (tagged [this _ _] (throw (except-unsupported this "PSchemeNode/tagged")))
  (to-java [this] (SSymbolLiteralNodeFactory$ReadLocalNodeGen/create int-key)))

(defrecord ArgDispatch [position rest-arg?]
  PSchemeNode
  (specialize [this] (throw (except-unsupported this "PSchemeNode/specialize")))
  (tagged [this _ _] (throw (except-unsupported this "PSchemeNode/tagged")))
  (to-java [this]
    (if rest-arg?
      (SSymbolLiteralNode$ReadArgDispatch. (SReadVarArgsNode. position)) ; after pos, which is also the # of args preceding it
      (SSymbolLiteralNode$ReadArgDispatch. (SReadArgSlotNode. position)))))

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
                (SExactNumberNode. (BigDecimal. (BigInteger. ^String uint-str ^int radix)))
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
    (let [val (cond exact? (SExactNumberNode. ^BigDecimal (BigDecimal. ^String decimal-str))
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
                         (to-java read-var-dispatch))))

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

(defmulti detect-slot-kind
  "Given a node, returns an appropriate FrameSlotKind for that node"
  (fn [node] (type node)))

(defrecord LetNode [bindings body-forms]
  PSchemeNode
  (specialize [this]
    (->LetNode
      (mapv (fn [[sym val]] [sym (specialize val)]) bindings)
      (map specialize body-forms)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (let [sc->new-dispatch
          (->> bindings
               (map (fn [[sym value]]
                      [(:utf32codepoints sym) (->LocalDispatch
                                                (.addSlot frame-desc-builder,
                                                          (detect-slot-kind value),
                                                          nil, nil))]))
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

(defrecord LambdaNode [arguments body-forms frame-desc-builder]
  PSchemeNode
  (specialize [this]
    (LambdaNode. arguments
                 ;; a little trick I learnt from SimpleLanguage:
                 ;;  arguments, unlike slots, don't have a mechanism for specializations
                 ;;  which is why we store them in local slots
                 [(specialize (->LetNode
                                (partition 2 (interleave arguments arguments))
                                body-forms))]
                 frame-desc-builder))
  (tagged [this symbol-codepoints->dispatch _parent-frame-desc-builder]
    (let [sc->new-dispatch (reduce
                             (fn [sc->d a] (assoc sc->d (:utf32codepoints a) (:read-var-dispatch a)))
                             symbol-codepoints->dispatch
                             arguments)]
      (LambdaNode. arguments
                   (mapv #(tagged % sc->new-dispatch frame-desc-builder) body-forms)
                   frame-desc-builder)))
  (to-java [this]
    (SLambdaNode. (into-array SSymbolLiteralNode (map to-java arguments))
                  (node-array (map to-java body-forms))
                  (.build frame-desc-builder))))

(defn ->LambdaNode [arguments body-forms]
  (LambdaNode. arguments body-forms (FrameDescriptor/newBuilder (count arguments))))

(defmulti specialize-list
  "Return a special form node if the args given match
  the pattern of a syntax, otherwise returns nil"
  (fn [special-syntax-name & rest-nodes]
    special-syntax-name))

(defrecord ListNode [forms dotted?]
  PSchemeNode
  (specialize [this]
    (let [[f & rs] forms]
      (if (instance? SymbolLiteral f)
        (let [cps (int-array (:utf32codepoints f))
              as-str (String. cps 0 (count cps))]
          (if-let [spec-form (apply specialize-list as-str rs)]
            (specialize spec-form)                          ; specialize children nodes too
            (->ListNode (map specialize forms) dotted?)))
        (->ListNode (map specialize forms) dotted?))))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder]
    (->ListNode (map #(tagged % symbol-codepoints->dispatch frame-desc-builder)
                     forms)
                dotted?))
  (to-java [this]
    (let [[f & rs] forms
          rs (if dotted? rs (concat rs [(->NilLiteral)]))
          f (to-java f)
          rs (map to-java rs)]
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

(defmethod specialize-list "lambda" [_lambda & args]
  (when (>= (count args) 2)
    (let [formals (first args)
          body-forms (rest args)]
      (cond
        (instance? NilLiteral formals) (->LambdaNode [] body-forms)

        (and (instance? ListNode formals)
             (every? (partial instance? SymbolLiteral)
                     (:forms formals))
             (all-unique? (map :utf32codepoints (:forms formals))))
        (let [last-rest? (:dotted? formals)
              formals (map-indexed (fn [i a] (assoc a :read-var-dispatch (->ArgDispatch i false)))
                                   (:forms formals))
              bl (butlast formals)
              l (last formals)
              l-dispatch (:read-var-dispatch l)
              formals (conj (vec bl) (if last-rest?
                                       (assoc l :read-var-dispatch (assoc l-dispatch :rest-arg? true))
                                       l))]
          (->LambdaNode formals
                        body-forms))

        (instance? SymbolLiteral formals)
        (->LambdaNode [(assoc formals :read-var-dispatch (->ArgDispatch 0 true))]
                      body-forms)

        :else nil))))

(defmethod specialize-list :default [& _] nil)

(defmethod detect-slot-kind FalseLiteral [_] FrameSlotKind/Boolean)

(defmethod detect-slot-kind TrueLiteral [_] FrameSlotKind/Boolean)

(defmethod detect-slot-kind IntegerLiteral [n]
  (if (:exact? n)
    FrameSlotKind/Object
    FrameSlotKind/Long))

(defmethod detect-slot-kind FractionLiteral [_] FrameSlotKind/Object)

(defmethod detect-slot-kind DecimalLiteral [n]
  (match [(:exact? n) (:exp-mark n)]
    [true _] FrameSlotKind/Object
    [_ "s"] FrameSlotKind/Float
    [_ "S"] FrameSlotKind/Float
    [_ "f"] FrameSlotKind/Float
    [_ "F"] FrameSlotKind/Float
    :else FrameSlotKind/Double))

(defmethod detect-slot-kind NanInfLiteral [_] FrameSlotKind/Float)

(defmethod detect-slot-kind OctetLiteral [_] FrameSlotKind/Byte)

(defmethod detect-slot-kind :default [_] FrameSlotKind/Illegal)