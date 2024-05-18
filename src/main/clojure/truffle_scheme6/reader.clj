(ns truffle-scheme6.reader
  (:require [instaparse.core :as insta]
            [truffle-scheme6.number-transformers :refer [transform-number]]
            [clojure.core.match :refer [match]]
            [clojure.zip :as zip])
  (:import (java.util ArrayList List)
           (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.atoms SSymbolLiteralNode SNilLiteralNode SCharacterLiteralNode SStringLiteralNode)
           (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SOctetLiteralNode)
           (truffle_scheme6.nodes.composites SByteVectorLiteralNode SListNode SVectorLiteralNode)
           (truffle_scheme6.nodes.roots SchemeRoot)
           (truffle_scheme6.nodes.special SBeginNode SDefineVarNode SIfNode SQuoteNode)))

(insta/defparser parser
  "
  <expressions> = whitespace? (expression (whitespace expression)*)? whitespace?
  <expression> = comment / reader / composite / atom

  (* READER SYNTAX *)
  <reader> = quote / quasiquote / unquote / unquote-splicing / syntax / quasisyntax / unsyntax / unsyntax-splicing
  quote = <'\\''> expression
  quasiquote = <'`'> expression
  unquote = <','> expression
  unquote-splicing = <',@'> expression
  syntax = <'#\\''> expression
  quasisyntax = <'#`'> expression
  unsyntax = <'#,'> expression
  unsyntax-splicing = <'#,@'> expression

  (* COMPOSITES *)
  <composite> = bytevector / vector / list
  bytevector = <'#vu8('> octets? <')'>
  <octets> = whitespace? (octet (whitespace octet)*)? whitespace?
  octet = radix2 uinteger2 | radix8 uinteger8 | radix10 uinteger10 | radix16 uinteger16 (* I'll validate further at the node-producing level *)

  vector = <'#('> expressions? <')'>

  list = opar expressions? cpar | obr expressions? cbr | opar expressions whitespace '.' whitespace expression whitespace? cpar | obr expressions whitespace '.' whitespace expression whitespace? cbr
  <opar> = <'('>
  <cpar> = <')'>
  <obr> = <'['>
  <cbr> = <']'>

  (* ATOMS *)
  <atom> = bool / number / symbol / string / character

  <bool> = true | false
  true = <'#t'> | <'#T'>
  false = <'#f'> | <'#F'>

    (* symbols *)
  symbol = symbol-initial symbol-subsequent* | peculiar-symbol

  <symbol-initial> = symbol-constituent | symbol-special-initial | inline-hex-escape
  <symbol-constituent> = #'[A-Za-z]' | #'[\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Mn}\\p{Nl}\\p{No}\\p{Pd}\\p{Pc}\\p{Po}\\p{Sc}\\p{Sm}\\p{Sk}\\p{So}\\p{Co}&&[^\\x00-\\x7F]]'
  <symbol-special-initial> = #'[!$%&*/:<=>?^_~]'
  inline-hex-escape = <'\\\\x'> hex-scalar-value <';'>
  <hex-scalar-value> = #'[0-9a-fA-F]+'

  <symbol-subsequent> = symbol-initial | #'[0-9]' | #'[\\p{Nd}\\p{Mc}\\p{Me}]' | symbol-special-subsequent
  <symbol-special-subsequent> = #'[+-.@]'

  <peculiar-symbol> = '+' | '-' | '...' | '->' symbol-subsequent*

    (* strings *)
  string = <'\"'> string-element* <'\"'>
  <string-element> = #'[^\"\\\\]' | '\\\\a' | '\\\\b' | '\\\\t' | '\\\\n' | '\\\\v' | '\\\\f' | '\\\\r' | '\\\\\"' | '\\\\\\\\' | #'\\p{Zs}' | <#'\\\\\\s*\\n\\s*'> | inline-hex-escape

    (* characters *)
  character = '#\\\\' #'.' | '#\\\\' character-name | '#\\\\x' utf8-hex-scalar
  character-name = 'nul' | 'alarm' | 'backspace' | 'tab' | 'linefeed' | 'newline' | 'vtab' | 'page' | 'return' | 'esc' | 'space' | 'delete'
  <utf8-hex-scalar> = #'[0-9a-fA-F]{1,8}'

    (* numbers *)
  number = num2 / num8 / num10 / num16

  <num2> = prefix2 complex2
  <num8> = prefix8 complex8
  <num10> = prefix10 complex10
  <num16> = prefix16 complex16

  complex2 =   real2 |  real2 '@' real2  |  real2 '+' ureal2  'i' |  real2 '-' ureal2  'i' |  real2 '+' naninf 'i' |  real2 '-' naninf 'i' |  real2 '+' 'i' |  real2 '-' 'i' | '+' ureal2  'i' | '-' ureal2  'i' | '+' naninf 'i' | '-' naninf 'i' | '+' 'i' | '-' 'i'
  complex8 =   real8 |  real8 '@' real8  |  real8 '+' ureal8  'i' |  real8 '-' ureal8  'i' |  real8 '+' naninf 'i' |  real8 '-' naninf 'i' |  real8 '+' 'i' |  real8 '-' 'i' | '+' ureal8  'i' | '-' ureal8  'i' | '+' naninf 'i' | '-' naninf 'i' | '+' 'i' | '-' 'i'
  complex10 = real10 | real10 '@' real10 | real10 '+' ureal10 'i' | real10 '-' ureal10 'i' | real10 '+' naninf 'i' | real10 '-' naninf 'i' | real10 '+' 'i' | real10 '-' 'i' | '+' ureal10 'i' | '-' ureal10 'i' | '+' naninf 'i' | '-' naninf 'i' | '+' 'i' | '-' 'i'
  complex16 = real16 | real16 '@' real16 | real16 '+' ureal16 'i' | real16 '-' ureal16 'i' | real16 '+' naninf 'i' | real16 '-' naninf 'i' | real16 '+' 'i' | real16 '-' 'i' | '+' ureal16 'i' | '-' ureal16 'i' | '+' naninf 'i' | '-' naninf 'i' | '+' 'i' | '-' 'i'

  real2  = sign ureal2  | '+' naninf | '-' naninf
  real8  = sign ureal8  | '+' naninf | '-' naninf
  real10 = sign ureal10 | '+' naninf | '-' naninf
  real16 = sign ureal16 | '+' naninf | '-' naninf

  naninf = 'nan.0' | 'inf.0'

  ureal2 = uinteger2 | fraction2
  ureal8 = uinteger8 | fraction8
  ureal10 = uinteger10 / fraction10 / decimal10 mantissa-width
  ureal16 = uinteger16 | fraction16

  <fraction2> = uinteger2 '/' uinteger2
  <fraction8> = uinteger8 '/' uinteger8
  <fraction10> = uinteger10 '/' uinteger10
  <fraction16> = uinteger16 '/' uinteger16

  decimal10 = uinteger10 suffix | '.' digit10+ suffix | digit10+ '.' digit10* suffix | digit10+ '.' suffix

  uinteger2 = digit2+
  uinteger8 = digit8+
  uinteger10 = digit10+
  uinteger16 = digit16+

  prefix2 = radix2 exactness | exactness radix2
  prefix8 = radix8 exactness | exactness radix8
  prefix10 = radix10 exactness | exactness radix10
  prefix16 = radix16 exactness | exactness radix16

  (* todo an exponent-marker for bigdecimal? *)
  suffix = empty | exponent-marker sign digit10+
  exponent-marker = 'e' | 'E' | 's' | 'S' | 'f' | 'F' | 'd' | 'D' | 'l' | 'L'
  mantissa-width = empty | '|' digit10+
  sign = empty | '+' | '-'
  exactness = empty | '#i' | '#I' | '#e' | '#E'

  radix2 = '#b' | '#B'
  radix8 = '#o' | '#O'
  radix10 = empty | '#d' | '#D'
  radix16 = '#x' | '#X'

  <digit2> = #'[01]'
  <digit8> = #'[0-7]'
  <digit10> = #'[0-9]'
  <digit16> = #'[0-9A-Fa-f]'

  (* USELESS STUFF *)
  <comment> = <single-line-comment> / <block-comment> / <form-comment> / '#!r6rs'
  single-line-comment = #';.*'                                                   (* dot doesn't match line breaks *)
  block-comment = '#|' ( block-comment / #'[^|#]' / !'#' '|' !'#' / !'|' '#' !'|' )* '|#'
  form-comment = '#;' ( whitespace? | comment? )* expression

  <whitespace> = <#'\\s+'>

  (* MISC *)
  <empty> = ''
  ")

(def named-chars {:nul       0x0000
                  :alarm     0x0007
                  :backspace 0x0008
                  :tab       0x0009
                  :linefeed  0x000A
                  :newline   0x000A
                  :vtab      0x000B
                  :page      0x000C
                  :return    0x000D
                  :esc       0x001B
                  :space     0x0020
                  :delete    0x007F})

(def string-escapes {"\\a"  0x0007
                     "\\b"  0x0008
                     "\\t"  0x0009
                     "\\n"  0x000A
                     "\\v"  0x000B
                     "\\f"  0x000C
                     "\\r"  0x000D
                     "\\\"" 0x0022})

(def define-tag "define-")
(def lambda-tag "lambda-")

(declare produce-nodes)

(defn node-array
  [aseq]
  (into-array SchemeNode aseq))

(defn symnode->string
  [symbol-node]
  (-> symbol-node (.getSymbol) (.getValue) (.toJavaStringUncached)))

(defn symnode?
  [node]
  (instance? SSymbolLiteralNode node))

(defn tagged?
  ([tag node] (and (sequential? node) (= tag (first node))))
  ([tag] #(tagged? tag %)))

(defn- symbol-vec
  [s]
  (reduce conj [:symbol] (map str s)))

(defn general-zipper
  [tree]
  (zip/zipper #(or (vector? %) (seq? %))
              seq
              (fn [node children]
                (with-meta (if (seq? node)
                             children
                             (vec children))
                           (meta node)))
              tree))

(defn zip-walk
  [f z]
  (if (zip/end? z)
    (zip/root z)
    (recur f (zip/next (f z)))))

(defn edit-node-tags
  [f tree]
  (let [z (general-zipper tree)]
    (zip-walk (fn [node]
                (cond
                  (zip/branch? node) node
                  (and (keyword? (first node))
                       (empty? (zip/lefts node))) (zip/edit node f)
                  :else node))
              z)))

(defn prefix-nodes
  [prefix tree]
  (edit-node-tags
    (fn [k] (keyword (str prefix (name k))))
    tree))

(defn unprefix-nodes
  [prefix tree]
  (edit-node-tags
    (fn [k] (keyword (apply str (drop (count prefix) (name k)))))
    tree))

(defn formalize-list
  [formals]
  (let [clean-formals (remove #{"."} (rest formals))
        build-slots (fn [args] (reduce #(conj %1 [:s-formal-slot %2]) [:s-formals] args))]
    (cond
      (and (tagged? :list formals) (some #{"."} formals))
      (-> (build-slots (butlast clean-formals))
          (conj [:s-formal-varargs (last clean-formals) (dec (count clean-formals))]))

      (tagged? :list formals)
      (build-slots clean-formals)

      :else (throw (Exception. (str "Malformed formals list: " formals))))))

(defmulti tag-special-list
  (fn [& args]
    (let [[f & _r :as args] args]
      f)))

(defmethod tag-special-list (symbol-vec "define") [_define-sym & args]
  (match (vec args)
    [(var-name :guard (tagged? :symbol))]
    [:s-define-var [:s-define-name var-name] [:s-define-value nil]]

    [(var-name :guard (tagged? :symbol)) value]
    [:s-define-var [:s-define-name var-name] [:s-define-value value]]

    [(spec :guard (tagged? :list))
     & [body-first & body-rest]]
    (let [spec (rest spec)]
      (match (vec spec)
        [(var-name :guard (tagged? :symbol)) "." (varargs-name :guard (tagged? :symbol))]
        [:s-define-proc
         [:s-define-name var-name]
         [:s-formals [:s-formal-varargs varargs-name 0]]
         (reduce conj [:s-define-proc-body body-first] body-rest)]

        [(var-name :guard (tagged? :symbol)) (formals :guard (tagged? :list))]
        [:s-define-proc
         [:s-define-name var-name]
         (formalize-list formals)
         (reduce conj [:s-define-proc-body body-first] body-rest)]

        :else (throw (Exception. (str "Wrong syntax: " "Malformed define form")))))

    :else (throw (Exception. (str "Wrong syntax: " "Malformed define form")))))

(defmethod tag-special-list (symbol-vec "lambda") [_lambda-sym & args]
  (when (<= (count args) 1)
    (throw (throw (Exception. (str "Wrong syntax: " "Malformed define form")))))
  (let [[formals & body :as args] args
        body-node (reduce conj [:s-lambda-body] body)]
    (cond
      (tagged? :symbol formals)
      [:s-lambda
       [:s-formals [:s-formal-varargs formals 0]]
       body-node]

      (tagged? :list formals)
      [:s-lambda
       (formalize-list formals)
       body-node]

      :else (throw (Exception. (str "Wrong syntax: " "Malformed lambda form"))))))

(defmethod tag-special-list (symbol-vec "if") [_if-sym & args]
  (let [[condition then else] args]
    (condp = (count args)
      0 (throw (IllegalArgumentException. "Not enough args given to if"))
      1 (throw (IllegalArgumentException. "Not enough args given to if"))
      2 [:s-if [:s-if-condition condition] [:s-if-then then] [:s-if-else nil]]
      3 [:s-if [:s-if-condition condition] [:s-if-then then] [:s-if-else else]]
      (throw (IllegalArgumentException. "Too many args given to if")))))

(defmethod tag-special-list (symbol-vec "begin") [_begin-sym & args]
  (prn _begin-sym args)
  (when (empty? args)
    (throw (IllegalArgumentException. "Can't create begin form with no body")))
  (reduce conj [:s-begin] args))

(defmethod tag-special-list :default [& args]
  (reduce conj [:list] args))

(defn transform-symbol
  [& args]
  (SSymbolLiteralNode. (->> args
                            (map (fn [str|cpoint]
                                   (if (string? str|cpoint)
                                     (vec (.toList (.boxed (.codePoints str|cpoint))))
                                     [str|cpoint])))
                            (flatten)
                            (int-array))
                       ; var dispatch is initialized by the forms that initialize those contexts
                       nil))

(defn transform-string
  [& args]
  (->> args
       (map (fn [elm]
              (cond
                (some? (string-escapes elm)) [(string-escapes elm)]
                (string? elm) (vec (.toList (.boxed (.codePoints elm))))
                :else [elm])))
       (flatten)
       (int-array)
       (SStringLiteralNode.)))

(defn transform-character
  [& args]
  (cond
    (= (first args) "#\\x")
    (let [n (Integer/parseUnsignedInt (second args) 16)]
      (SCharacterLiteralNode. n))

    (and (vector? (second args)) (= (first (second args)) :character-name))
    (let [char-name (second (second args))]
      (SCharacterLiteralNode. ^int (named-chars (keyword char-name))))

    (= 1 (count (second args)))
    (SCharacterLiteralNode. ^char (.charAt (second args) 0))))

(defn transform-list
  [args]
  (let [[form & args] args]
    (SListNode. form
                (node-array (concat args [(SNilLiteralNode.)])))))

(defn transform-quote
  [& args]
  (if (not= 1 (count args))
    (throw (Exception. "Wrong syntax: quote given wrong amount of arguments"))
    (SQuoteNode. (first args))))

(defn transform-quoted-list
  [& args]
  (cond
    (empty? args) (SNilLiteralNode.)
    (some #{"."} args) (let [args (remove #{"."} args)]
                         (SListNode. (first args)
                                     (node-array (rest args))))
    :else (let [args (conj (vec args) (SNilLiteralNode.))]
            (SListNode. (first args)
                        (node-array (rest args))))))

(defn transform-octet
  [[radix] [_int-kword & digits]]
  (let [r (condp = radix
            :radix2 2 :radix8 8
            :radix10 10 :radix16 16)
        val (apply str digits)
        n (Long/parseUnsignedLong val r)]
    (if (and (>= n 0) (<= n 255))
      (SOctetLiteralNode. (unchecked-byte n))
      (throw (Exception. "Octet given was not valid. Has to be an unsigned 8-bit integer")))))

(defn transform-define-var-form
  [args]
  (let [[[_ name] [_ value]] args]
    (SDefineVarNode. name value)))

(defn transform-if-form
  [args]
  (let [[[_ condition] [_ then] [_ else]] args]
    (SIfNode. condition then else)))

(defn transform-begin-form
  [args]
  (SBeginNode. (node-array args)))

(defn parse
  [^CharSequence source & {:keys [starting-at]}]
  (let [parse-strictly (if starting-at #(parser % :start starting-at) parser)]
    (->> source
         parse-strictly)))

(defn- tag-quotes
  [ast]
  (->> ast
       (insta/transform
         {:list (fn [& args]
                  (cond
                    (empty? args) [:list]                   ; the nil case is handled later on by transform-list
                    (= (symbol-vec "quote") (first args)) (reduce conj [:quote] (rest args))
                    :else (reduce conj [:list] args)))})
       (insta/transform
         {:quote (fn [& args]
                   (reduce conj [:quote] (insta/transform
                                           {:quote (fn [& args]
                                                     (reduce conj
                                                             [:quoted-list
                                                              (symbol-vec "quote")]
                                                             args))
                                            :list  (fn [& args]
                                                     (reduce conj
                                                             [:quoted-list]
                                                             args))}
                                           args)))})))

(defn- tag-specials
  [ast]
  (->> ast
       (insta/transform
         {:list tag-special-list})))

(defn- produce-nodes
  [ast]
  (insta/transform
    {:list              transform-list
     :vector            #(SVectorLiteralNode. (node-array %&))
     :bytevector        #(SByteVectorLiteralNode. (into-array SOctetLiteralNode %&))

     :quote             transform-quote
     :quoted-list       transform-quoted-list

     :s-define-var      transform-define-var-form
     :s-if              transform-if-form
     :s-begin           transform-begin-form

     :number            transform-number
     :octet             transform-octet
     :symbol            transform-symbol
     :string            transform-string
     :inline-hex-escape #(Integer/parseUnsignedInt % 16)
     :character         transform-character
     :true              (fn [& r] (STrueLiteralNode.))
     :false             (fn [& r] (SFalseLiteralNode.))}
    ast))

(defn read-scheme
  [source]
  (->> source
       (parse)
       (tag-quotes)
       (tag-specials)
       (produce-nodes)))