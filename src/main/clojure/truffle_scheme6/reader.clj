(ns truffle-scheme6.reader
  (:require [instaparse.core :as insta]
            [truffle-scheme6.number-transformers :refer [transform-number]])
  (:import (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.atoms SIdentifierLiteralNode SNilLiteralNode SCharacterLiteralNode SStringLiteralNode)
           (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)
           (truffle_scheme6.nodes.atoms.numbers SOctetLiteralNode)
           (truffle_scheme6.nodes.composites SByteVectorLiteralNode SListNode SVectorLiteralNode)
           (truffle_scheme6.nodes.special SIfNode SQuoteNode)))

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
  <atom> = bool / number / identifier / string / character

  <bool> = true | false
  true = <'#t'> | <'#T'>
  false = <'#f'> | <'#F'>

    (* identifiers *)
  identifier = identifier-initial identifier-subsequent* | peculiar-identifier

  <identifier-initial> = identifier-constituent | identifier-special-initial | inline-hex-escape
  <identifier-constituent> = #'[A-Za-z]' | #'[\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Mn}\\p{Nl}\\p{No}\\p{Pd}\\p{Pc}\\p{Po}\\p{Sc}\\p{Sm}\\p{Sk}\\p{So}\\p{Co}&&[^\\x00-\\x7F]]'
  <identifier-special-initial> = #'[!$%&*/:<=>?^_~]'
  inline-hex-escape = <'\\\\x'> hex-scalar-value <';'>
  <hex-scalar-value> = #'[0-9a-fA-F]+'

  <identifier-subsequent> = identifier-initial | #'[0-9]' | #'[\\p{Nd}\\p{Mc}\\p{Me}]' | identifier-special-subsequent
  <identifier-special-subsequent> = #'[+-.@]'

  <peculiar-identifier> = '+' | '-' | '...' | '->' identifier-subsequent*

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
  block-comment = '#|' ( block-comment / #'((?!\\|#)(.|\\n))' )* '|#'
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

(defn transform-identifier
  [& args]
  (->> args
       (map (fn [str|cpoint]
              (if (string? str|cpoint)
                (vec (.toList (.boxed (.codePoints str|cpoint))))
                [str|cpoint])))
       (flatten)
       (int-array)
       (SIdentifierLiteralNode.)))

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

(defmulti transform-list
          (fn [& args]
            (let [[f & _r :as args] args]
              (cond
                (empty? args) :nil
                (instance? SIdentifierLiteralNode f) (-> f (.getValue) (.toJavaStringUncached))
                :else :default))))

(defmethod transform-list :nil [& _args]
  (SNilLiteralNode.))

(defmethod transform-list "quote" [_quote-sym & args]
  (condp = (count args)
    ; todo special malformed form exceptions
    0 (throw (IllegalArgumentException. "No args given to quote"))
    1 (SQuoteNode. (first args))
    (throw (IllegalArgumentException. "Too many args given to quote"))))

(defmethod transform-list "if" [_if-sym & args]
  (let [[condition then else] args]
    (condp = (count args)
      0 (throw (IllegalArgumentException. "Not enough args given to if"))
      1 (throw (IllegalArgumentException. "Not enough args given to if"))
      2 (SIfNode. condition then nil)
      3 (SIfNode. condition then else)
      (throw (IllegalArgumentException. "Too many args given to if")))))

(defmethod transform-list :default [& args]
  (let [[form & args] args]
    (SListNode. form
                (into-array SchemeNode
                            (if (some #(= "." %) args)
                              (remove #(= "." %) args)
                              (concat args [(SNilLiteralNode.)]))))))

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

(defn parse
  [^CharSequence source & {:keys [starting-at]}]
  (let [parse-strictly (if starting-at #(parser % :start starting-at) parser)]
    (->> source
         parse-strictly)))

(defn produce-nodes
  [ast]
  (insta/transform {:list              transform-list
                    :vector            #(SVectorLiteralNode. (into-array SchemeNode %&))
                    :bytevector        #(SByteVectorLiteralNode. (into-array SOctetLiteralNode %&))

                    :number            transform-number
                    :octet             transform-octet
                    :identifier        transform-identifier
                    :string            transform-string
                    :inline-hex-escape #(Integer/parseUnsignedInt % 16)
                    :character         transform-character
                    :true              (fn [& r] (STrueLiteralNode.))
                    :false             (fn [& r] (SFalseLiteralNode.))}
                   ast))