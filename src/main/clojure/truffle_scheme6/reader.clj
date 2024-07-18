(ns truffle-scheme6.reader
  (:require [clojure.core.match :refer [match]]
            [instaparse.core :as insta]
            [truffle-scheme6.number-transformers :refer [transform-number]]
            [truffle-scheme6.parser-types :refer :all]))

(insta/defparser
  parser
  "
  <root-expressions> = whitespace? (expression (whitespace expression)*)? whitespace?
  <expressions> = whitespace? (expression (whitespace expression)*) whitespace?
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

(defn- symbol-vec
  [s]
  (reduce conj [:symbol] (map str s)))

(defn transform-symbol
  [& args]
  (->SymbolLiteral
    (->> args
         (map (fn [str|cpoint]
                (if (string? str|cpoint)
                  (vec (.toList (.boxed (.codePoints str|cpoint))))
                  [str|cpoint])))
         (flatten))))

(def string-escapes {"\\a"  0x0007
                     "\\b"  0x0008
                     "\\t"  0x0009
                     "\\n"  0x000A
                     "\\v"  0x000B
                     "\\f"  0x000C
                     "\\r"  0x000D
                     "\\\"" 0x0022
                     "\\\\" 0x005C})
(defn transform-string
  [& args]
  (->> args
       (map (fn [elm]
              (cond
                (some? (string-escapes elm)) [(string-escapes elm)]
                (string? elm) (vec (.toList (.boxed (.codePoints elm))))
                :else [elm])))
       (flatten)
       (->StringLiteral)))

(def named-chars {"nul"       0x0000
                  "alarm"     0x0007
                  "backspace" 0x0008
                  "tab"       0x0009
                  "linefeed"  0x000A
                  "newline"   0x000A
                  "vtab"      0x000B
                  "page"      0x000C
                  "return"    0x000D
                  "esc"       0x001B
                  "space"     0x0020
                  "delete"    0x007F})
(defn transform-character
  [& args]
  (->CharacterLiteral
    (match (vec args)
      ["#\\x" uint-str] (Integer/parseUnsignedInt uint-str)
      ["#\\" [:character-name c-name]] (named-chars c-name)
      ["#\\" c] (.codePointAt c 0))))

(defn transform-list
  [& args]
  (let [[form & args] args]
    (->ListNode form
                (concat args [(->NilLiteral)]))))

(defn transform-vector
  [& args]
  (->VectorNode args))

(defn transform-bytevector
  [& args]
  (->ByteVectorNode args))

(defn transform-quote
  [& args]
  (if (not= 1 (count args))
    (throw (Exception. "Wrong syntax: quote given wrong amount of arguments"))
    (->QuoteNode (first args))))

(defn transform-quoted-list
  [& args]
  (cond
    (empty? args) (->NilLiteral)
    (some #{"."} args) (let [args (remove #{"."} args)]
                         (->ListNode (first args) (rest args)))
    :else (let [args (conj (vec args) (->NilLiteral))]
            (->ListNode (first args) (rest args)))))

(defn transform-octet
  [[radix] [_int-kword & digits]]
  (let [r (condp = radix
            :radix2 2 :radix8 8
            :radix10 10 :radix16 16)]
    (->OctetLiteral radix
                    (apply str digits))))

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

(defn- produce-nodes
  [ast]
  (insta/transform
    {:inline-hex-escape #(Integer/parseUnsignedInt % 16)

     :number            transform-number
     :true              ->TrueLiteral
     :false             ->FalseLiteral
     :character         transform-character
     :string            transform-string
     :symbol            transform-symbol
     :octet             transform-octet

     :quote             transform-quote
     :quoted-list       transform-quoted-list

     :list              transform-list
     :vector            transform-vector
     :bytevector        transform-bytevector}
    ast))

(defn read-scheme
  [source]
  (->> source
       (parse)
       (tag-quotes)
       (produce-nodes)))

