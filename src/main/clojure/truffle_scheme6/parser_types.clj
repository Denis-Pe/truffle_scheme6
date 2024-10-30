(ns truffle-scheme6.parser-types
  (:require [clojure.core.match :refer [match]])
  (:import (com.oracle.truffle.api.frame FrameDescriptor FrameSlotKind)
           (java.math MathContext)
           (org.graalvm.collections Pair)
           (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.atoms SCharacterLiteralNode SNilLiteralNode SStringLiteralNode SSymbolLiteralNode SSymbolLiteralNode$ReadFromMaterialized SSymbolLiteralNode$ReadArgDispatch SSymbolLiteralNode$ReadGlobal SSymbolLiteralNodeFactory$ReadLocalNodeGen)
           (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SComplexLiteralNode SExactBigIntegerNode SExactRealNode SFractionLiteralNode SExactFixnumNode SInexactReal32Node SInexactReal64Node SOctetLiteralNode)
           (truffle_scheme6.nodes.composites SByteVectorLiteralNode SListNode SVectorLiteralNode)
           (truffle_scheme6.nodes.functions SReadArgSlotNode SReadVarArgsNode)
           (truffle_scheme6.nodes.special SBeginNode SDefineVarNode SDefunNode SLambdaNode SLetNode SQuoteNode SSetClosureNode SSetGlobalNode SSetLocalNode)))

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

(defn parse-arbitrary-integer
  "If a value fits in a long number,
  returns a long, otherwise BigInteger"
  ([^String s] (parse-arbitrary-integer s 10))
  ([^String s radix] (try (Long/parseLong s radix)
                          (catch NumberFormatException _e
                            (BigInteger. ^String s ^int radix)))))

(defprotocol PSchemeNode
  ; organized in the order that they would be run from a top-level parser
  (specialize [this] "Transforms a list node into an appropriate special node, otherwise returns the same thing")
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names] "Transform child symbols according to specified dispatch. Should be run on child nodes")
  (to-java [this] "Transforms a given node to a Java object"))

(defn except-unsupported [node op-name]
  (UnsupportedOperationException.
    (str op-name " not supported on node " node " of type " (type node))))

(defrecord ReadGlobalDispatch []
  PSchemeNode
  (specialize [this] (throw (except-unsupported this "PSchemeNode/specialize")))
  (tagged [this _ _ _] (throw (except-unsupported this "PSchemeNode/tagged")))
  (to-java [this] (SSymbolLiteralNode$ReadGlobal.)))

(defrecord ReadLocalDispatch [frame-name int-key]
  PSchemeNode
  (specialize [this] (throw (except-unsupported this "PSchemeNode/specialize")))
  (tagged [this _ _ _] (throw (except-unsupported this "PSchemeNode/tagged")))
  (to-java [this] (SSymbolLiteralNodeFactory$ReadLocalNodeGen/create int-key)))

(defrecord ReadClosureDispatch [frame-name int-key]
  PSchemeNode
  (specialize [this] (throw (except-unsupported this "PSchemeNode/specialize")))
  (tagged [this _ _ _] (throw (except-unsupported this "PSchemeNode/tagged")))
  (to-java [this] (SSymbolLiteralNode$ReadFromMaterialized. frame-name nil int-key)))

(defrecord ReadArgDispatch [position rest-arg?]
  PSchemeNode
  (specialize [this] (throw (except-unsupported this "PSchemeNode/specialize")))
  (tagged [this _ _ _] (throw (except-unsupported this "PSchemeNode/tagged")))
  (to-java [this]
    (if rest-arg?
      (SSymbolLiteralNode$ReadArgDispatch. (SReadVarArgsNode. position)) ; after pos, which is also the # of args preceding it
      (SSymbolLiteralNode$ReadArgDispatch. (SReadArgSlotNode. position)))))

(defrecord FalseLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (SFalseLiteralNode.)))

(defrecord TrueLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (STrueLiteralNode.)))

(defrecord IntegerLiteral [exact? radix sign uint-str]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (let [val (if (false? exact?)
                (SInexactReal64Node. (.doubleValue (BigInteger. ^String uint-str ^int radix)))
                (let [n (parse-arbitrary-integer uint-str radix)]
                  (if (instance? BigInteger n)
                    (SExactBigIntegerNode. n)
                    (SExactFixnumNode. n))))]
      (if (= sign "-")
        (.negate val)
        val))))

(defrecord FractionLiteral [numerator-int-literal denominator-int-literal]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (let [exact? (:exact? numerator-int-literal)
          sign (:sign numerator-int-literal)]
      (if (false? exact?)
        (let [num-bigdec (bigdec (parse-arbitrary-integer (:uint-str numerator-int-literal)
                                                          (:radix numerator-int-literal)))
              deno-bigdec (bigdec (parse-arbitrary-integer (:uint-str denominator-int-literal)
                                                           (:radix denominator-int-literal)))
              val (-> num-bigdec (.divide deno-bigdec MathContext/DECIMAL64) (.doubleValue) (SInexactReal64Node.))]
          (if (= sign "-") (.negate val) val))
        (SFractionLiteralNode. (to-java numerator-int-literal) (to-java denominator-int-literal))))))

(defrecord DecimalLiteral [exact? sign decimal-str exp-mark exp-val mantissa-width] ; mantissa is ignored for now
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
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
  (tagged [this _ _ _] this)
  (to-java [this]
    (let [num (condp = [sign literal]
                ["-" "inf.0"] Float/NEGATIVE_INFINITY
                ["+" "inf.0"] Float/POSITIVE_INFINITY
                Float/NaN)]
      (SInexactReal32Node. num))))

(defrecord ComplexLiteral [real-literal imaginary-literal]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (SComplexLiteralNode. (to-java real-literal) (to-java imaginary-literal))))

(defrecord OctetLiteral [radix octet-str]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (let [parsed (Integer/parseUnsignedInt octet-str radix)]
      (if (and (>= parsed 0) (<= parsed 255))
        (SOctetLiteralNode. (unchecked-byte parsed))
        (throw (Exception. "Octet given was not valid. Must be an unsigned 8-bit integer"))))))

(defrecord CharacterLiteral [utf32value]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (SCharacterLiteralNode. ^int utf32value)))

(defrecord StringLiteral [utf32codepoints]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (SStringLiteralNode. (int-array utf32codepoints))))

(defrecord SymbolLiteral [utf32codepoints read-var-dispatch]
  PSchemeNode
  (specialize [this] this)
  (tagged [this symbol-codepoints->dispatch _frame-desc-builder frame-names]
    (->SymbolLiteral
      utf32codepoints
      (if-let [d (symbol-codepoints->dispatch utf32codepoints)]
        (if (and (instance? ReadLocalDispatch d) (not= (last frame-names) (:frame-name d)))
          (->ReadClosureDispatch (last frame-names) (:int-key d))
          d)
        read-var-dispatch)))
  (to-java [this]
    (SSymbolLiteralNode. (int-array utf32codepoints)
                         (to-java read-var-dispatch))))

(defrecord NilLiteral []
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)
  (to-java [this]
    (SNilLiteralNode.)))

;; special nodes

(defrecord QuoteNode [x]
  PSchemeNode
  (specialize [this] this)
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (->QuoteNode (tagged x symbol-codepoints->dispatch frame-desc-builder frame-names)))
  (to-java [this]
    (SQuoteNode. (to-java x))))

(defmulti detect-slot-kind
  "Given a node, returns an appropriate FrameSlotKind for that node"
  (fn [node] (type node)))

(defrecord LetNode [bindings body-forms]
  PSchemeNode
  (specialize [this]
    (->LetNode
      (mapv (fn [[sym val]] [sym (specialize val)]) bindings)
      (map specialize body-forms)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (let [sc->new-dispatch
          (->> bindings
               (map (fn [[sym value]]
                      [(:utf32codepoints sym) (->ReadLocalDispatch
                                                (last frame-names)
                                                (.addSlot frame-desc-builder,
                                                          (detect-slot-kind value),
                                                          nil, nil))]))
               (reduce
                 (fn [m [codepoints dispatch]]
                   (assoc m codepoints dispatch))
                 symbol-codepoints->dispatch))]
      (->LetNode
        (mapv (fn [[sym val]] [(tagged sym sc->new-dispatch frame-desc-builder frame-names)
                               (tagged val symbol-codepoints->dispatch frame-desc-builder frame-names)])
              bindings)
        (map #(tagged % sc->new-dispatch frame-desc-builder frame-names)
             body-forms))))
  (to-java [this]
    (SLetNode.
      (into-array
        Pair
        (map (fn [[sym val]] (Pair/create (to-java sym)
                                          (to-java val)))
             bindings))
      (node-array (map to-java body-forms)))))

(defrecord SetNode [identifier value]
  PSchemeNode
  (specialize [this] (->SetNode identifier (specialize value)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (->SetNode (tagged identifier symbol-codepoints->dispatch frame-desc-builder frame-names)
               (tagged value symbol-codepoints->dispatch frame-desc-builder frame-names)))
  (to-java [this]
    (let [dispatch (:read-var-dispatch identifier)
          identifier (to-java identifier)
          value (to-java value)]
      (condp instance? dispatch
        ReadLocalDispatch (SSetLocalNode. identifier value)
        ReadClosureDispatch (SSetClosureNode. (:frame-name dispatch) identifier value)
        ReadGlobalDispatch (SSetGlobalNode. identifier value)))))

(defrecord DefineVarNode [identifier value]
  PSchemeNode
  (specialize [this] (->DefineVarNode identifier (specialize value)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (->DefineVarNode (tagged identifier symbol-codepoints->dispatch frame-desc-builder frame-names)
                     (tagged value symbol-codepoints->dispatch frame-desc-builder frame-names)))
  (to-java [this]
    (SDefineVarNode. (to-java identifier) (to-java value))))

(defrecord DefineUnspecifiedNode [identifier]
  PSchemeNode
  (specialize [this] (->DefineUnspecifiedNode identifier))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (->DefineUnspecifiedNode
      (tagged identifier symbol-codepoints->dispatch frame-desc-builder frame-names)))
  (to-java [this]
    (SDefineVarNode. (to-java identifier) nil)))

(defrecord DefineFunNode [identifier formals body-forms frame-desc-builder fun-name]
  PSchemeNode
  (specialize [this]
    (DefineFunNode. identifier
                    formals
                    [(specialize (->LetNode
                                   (partition 2 (interleave formals formals))
                                   body-forms))]
                    frame-desc-builder
                    fun-name))
  (tagged [this symbol-codepoints->dispatch _parent-frame-desc-builder frame-names]
    (let [sc->new-dispatch (reduce
                             (fn [sc->d a] (assoc sc->d (:utf32codepoints a) (:read-var-dispatch a)))
                             symbol-codepoints->dispatch
                             formals)
          frame-names (conj frame-names fun-name)]
      (DefineFunNode. identifier
                      formals
                      (mapv #(tagged % sc->new-dispatch frame-desc-builder frame-names) body-forms)
                      frame-desc-builder
                      fun-name)))
  (to-java [this]
    (SDefunNode. (to-java identifier)
                 (into-array SSymbolLiteralNode (map to-java formals))
                 (node-array (map to-java body-forms))
                 (.build frame-desc-builder)
                 fun-name)))

(defn ->DefineFunNode [identifier formals body-forms]
  (DefineFunNode. identifier
                  formals
                  body-forms
                  (FrameDescriptor/newBuilder (count formals))
                  (str (gensym "define-"))))

(defrecord BeginNode [nodes]
  PSchemeNode
  (specialize [this] (->BeginNode (map specialize nodes)))
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (->BeginNode (map #(tagged % symbol-codepoints->dispatch frame-desc-builder frame-names)
                      nodes)))
  (to-java [this]
    (SBeginNode. (node-array (map to-java nodes)))))

(defrecord LambdaNode [arguments body-forms frame-desc-builder lambda-name]
  PSchemeNode
  (specialize [this]
    (LambdaNode. arguments
                 ;; a little trick I learnt from SimpleLanguage:
                 ;;  arguments, unlike slots, don't have a mechanism for specializations
                 ;;  which is why we store them in local slots
                 [(specialize (->LetNode
                                (partition 2 (interleave arguments arguments))
                                body-forms))]
                 frame-desc-builder
                 lambda-name))
  (tagged [this symbol-codepoints->dispatch _parent-frame-desc-builder frame-names]
    (let [sc->new-dispatch (reduce
                             (fn [sc->d a] (assoc sc->d (:utf32codepoints a) (:read-var-dispatch a)))
                             symbol-codepoints->dispatch
                             arguments)
          frame-names (conj frame-names lambda-name)]
      (LambdaNode. arguments
                   (mapv #(tagged % sc->new-dispatch frame-desc-builder frame-names) body-forms)
                   frame-desc-builder
                   lambda-name)))
  (to-java [this]
    (SLambdaNode. (into-array SSymbolLiteralNode (map to-java arguments))
                  (node-array (map to-java body-forms))
                  (.build frame-desc-builder)
                  lambda-name)))

(defn ->LambdaNode [arguments body-forms]
  (LambdaNode. arguments
               body-forms
               (FrameDescriptor/newBuilder (count arguments))
               (str (gensym "lambda-"))))

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
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (->ListNode (map #(tagged % symbol-codepoints->dispatch frame-desc-builder frame-names)
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
  (tagged [this symbol-codepoints->dispatch frame-desc-builder frame-names]
    (->VectorNode (map #(tagged % symbol-codepoints->dispatch frame-desc-builder frame-names)
                       xs)))
  (to-java [this]
    (SVectorLiteralNode. (node-array (map to-java xs)))))

(defrecord ByteVectorNode [octets]
  PSchemeNode
  (specialize [this] this)
  (tagged [this _ _ _] this)                                ;; can't have anything other than octets at the parser level
  (to-java [this]
    (SByteVectorLiteralNode. (into-array SOctetLiteralNode (map to-java octets)))))

(defn parse-formals
  "Given a ListNode of formals for a function, returns
  a vector of the transformed symbols with the proper dispatch
  
  if the given ListNode is not a valid list of formals or is not a ListNode
  at all, returns nil"
  [formals]
  (if (and (instance? ListNode formals)
           (every? (partial instance? SymbolLiteral)
                   (:forms formals))
           (all-unique? (map :utf32codepoints (:forms formals))))
    (let [last-rest? (:dotted? formals)
          formals (map-indexed (fn [i a] (assoc a :read-var-dispatch (->ReadArgDispatch i false)))
                               (:forms formals))
          bl (butlast formals)
          l (last formals)
          l-dispatch (:read-var-dispatch l)]
      (conj (vec bl) (if last-rest?
                       (assoc l :read-var-dispatch (assoc l-dispatch :rest-arg? true))
                       l)))))

(defmethod specialize-list "quote" [_quote & args]
  (if (= 1 (count args))
    (->QuoteNode (first args))))

(defmethod specialize-list "define" [_define & args]
  (let [nargs (count args)]
    (cond (and (or (= 1 nargs) (= 2 nargs)) (instance? SymbolLiteral (first args)))
          (let [[identifier value] args]
            (if value
              (->DefineVarNode identifier value)
              (->DefineUnspecifiedNode identifier)))

          (and (>= nargs 1) (instance? ListNode (first args)))
          (let [[spec-list & body] args
                [identifier & formals] (:forms spec-list)
                formals (if formals (parse-formals (->ListNode formals (:dotted? spec-list))))]
            (if (and (instance? SymbolLiteral identifier) formals)
              (->DefineFunNode identifier formals body)))

          :else nil)))

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

(defmethod specialize-list "set!" [_set & args]
  (if (and (= (count args) 2)
           (instance? SymbolLiteral (first args)))
    (apply ->SetNode args)))

(defmethod specialize-list "lambda" [_lambda & args]
  (when (>= (count args) 2)
    (let [formals (first args)
          body-forms (rest args)]
      (cond
        (instance? NilLiteral formals) (->LambdaNode [] body-forms)

        (instance? ListNode formals)
        (if-let [formals (parse-formals formals)]
          (->LambdaNode formals body-forms))

        (instance? SymbolLiteral formals)
        (->LambdaNode [(assoc formals :read-var-dispatch (->ReadArgDispatch 0 true))]
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