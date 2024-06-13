(ns parser-test
  (:require [clojure.test :refer :all]
            [truffle-scheme6.reader :refer [parse]]))

(defn- failure?
  [o]
  (instance? instaparse.gll/failure-type o))

(defn- node?
  [node-type node]
  (= (first node) node-type))

;;; COMMENTS

(deftest r6rs-comment-success
  (is (= '("#!r6rs") (parse "#!r6rs"))))

(deftest line-comment-success
  (are [src parse-result] (= (parse src) parse-result)
    "; line comment" '()
    ";;;;; more important line comment" '()
    "; line comment\n#!r6rs" '("#!r6rs")))

(deftest block-comment-success
  (are [src parse-result] (= (parse src) parse-result)
    "#||#" '()
    "#| block comment |#" '()
    "#| #| nested block comment |# |#" '()
    "#| multiple\n#| line |#\r\n nested block comment |#" '()))

(deftest form-comment-success
  (are [src parse-result] (= (parse src) parse-result)
    "#; form-comment" '()
    "#;1" '()
    "#;    1" '()
    "#;     \r\n \n\t   1" '()
    "#; 1 #!r6rs" '("#!r6rs")))

(deftest comment-successes
  (r6rs-comment-success)
  (line-comment-success)
  (block-comment-success)
  (form-comment-success))

(deftest form-comment-failure
  (are [src] (failure? (parse src))
    "#;"
    "#;    "))

(deftest block-comment-failure
  (are [src] (failure? (parse src))
    "#| no closing"
    "   no opening |#"
    "#|    nested no opening |# |#"
    "#| #| nested no closing |#"))

(deftest comment-failures
  (form-comment-failure)
  (block-comment-failure))

;;; NUMBERS

(deftest uinteger2
  (are [src] (= (parse src :starting-at :uinteger2)
                (reduce conj [:uinteger2] (map str src)))
    "0"
    "0000"
    "0001"
    "1000"
    "1111")
  (are [src] (failure? (parse src :starting-at :uinteger2))
    "-012"
    "012"
    "210"
    "a0b"))

(deftest uinteger8
  (are [src] (= (parse src :starting-at :uinteger8)
                (reduce conj [:uinteger8] (map str src)))
    "01234567"
    "000000011"
    "76543210")
  (are [src] (failure? (parse src :starting-at :uinteger8))
    "-01234567"
    "0123456789"
    "0123456789abcdef"))

(deftest uinteger10
  (are [src] (= (parse src :starting-at :uinteger10)
                (reduce conj [:uinteger10] (map str src)))
    "0123456789"
    "000000011"
    "9876543210")
  (are [src] (failure? (parse src :starting-at :uinteger10))
    "0123456789a"
    "-0123456789"
    "0123456789abcdef"))

(deftest uinteger16
  (are [src] (= (parse src :starting-at :uinteger16)
                (reduce conj [:uinteger16] (map str src)))
    "0123456789abcdef"
    "0011"
    "fedcba9876543210")
  (are [src] (failure? (parse src :starting-at :uinteger16))
    "0123456789abcdefg"
    "-0011"))

(deftest number-prefixes
  (is (= [:prefix2 [:radix2 "#b"] [:exactness "#i"]] (parse "#b#i" :starting-at :prefix2)))
  (is (= [:prefix2 [:exactness] [:radix2 "#b"]] (parse "#b" :starting-at :prefix2)))
  (is (= [:prefix10 [:exactness "#E"] [:radix10]] (parse "#E" :starting-at :prefix10)))
  (is (= [:prefix10 [:exactness "#E"] [:radix10 "#d"]] (parse "#E#d" :starting-at :prefix10))))

(deftest number-suffixes
  (is (= [:suffix] (parse "" :starting-at :suffix)))
  (let [src "d+16"]
    (is (= [:suffix [:exponent-marker "d"] [:sign "+"] "1" "6"] (parse src :starting-at :suffix))))
  (is (failure? (parse "L" :starting-at :suffix))))

(deftest decimal10
  (let [parse #(parse % :starting-at :decimal10)]
    (is (= [:decimal10 [:uinteger10 "0"] [:suffix]]
           (parse "0")))
    (is (= [:decimal10 "2" "." "0" [:suffix [:exponent-marker "d"] [:sign "+"] "1" "0"]]
           (parse "2.0d+10")))
    (is (= [:decimal10 "." "1" "2" "3" "4" [:suffix]]
           (parse ".1234")))
    (is (= [:decimal10 "1" "2" "3" "4" "." [:suffix [:exponent-marker "L"] [:sign "-"] "1" "1" "1" "1"]]
           (parse "1234.L-1111")))))

(deftest ureal2-8-16
  (let [parse2 #(parse % :starting-at :ureal2)
        parse8 #(parse % :starting-at :ureal8)
        parse16 #(parse % :starting-at :ureal16)]
    (are [src] (= [:ureal2 (reduce conj [:uinteger2] (map str src))] (parse2 src))
      "0"
      "111")
    (are [src] (= [:ureal8 (reduce conj [:uinteger8] (map str src))] (parse8 src))
      "0"
      "01234567")
    (are [src] (= [:ureal16 (reduce conj [:uinteger16] (map str src))] (parse16 src))
      "0"
      "0123456789abcdefABCDEF")
    (is (= [:ureal2 [:uinteger2 "0"] "/" [:uinteger2 "1"]] (parse2 "0/1")))
    (is (= [:ureal8 [:uinteger8 "1" "2" "3"] "/" [:uinteger8 "0"]] (parse8 "123/0")))
    (is (= [:ureal16 [:uinteger16 "a"] "/" [:uinteger16 "1" "0" "b"]] (parse16 "a/10b")))
    (is (failure? (parse2 "1/")))
    (is (failure? (parse8 "5/")))
    (is (failure? (parse16 "d/")))
    (is (failure? (parse2 "/1")))
    (is (failure? (parse8 "/7")))
    (is (failure? (parse16 "/f")))))

(deftest ureal10
  (let [parse #(parse % :starting-at :ureal10)]
    (are [src] (= [:ureal10 (reduce conj [:uinteger10] (map str src))]
                  (parse src))
      "0"
      "1234567890")
    (is (= [:ureal10 [:uinteger10 "1"] "/" [:uinteger10 "9"]]
           (parse "1/9")))
    (is (failure? (parse "9/")))
    (is (failure? (parse "/9")))
    (is (= [:ureal10 [:decimal10 "2" "." [:suffix [:exponent-marker "s"] [:sign "+"] "1" "6"]] [:mantissa-width "|" "1" "2" "8"]]
           (parse "2.s+16|128")))
    (is (= [:ureal10 [:decimal10 "1" "." "5" [:suffix]] [:mantissa-width]]
           (parse "1.5")))))

(deftest ureals
  (ureal2-8-16)
  (ureal10))

(deftest naninf
  ; nan & inf
  (let [parse #(parse % :starting-at :real10)]
    (are [naninf-str] (= (parse naninf-str)
                         [:real10 (str (first naninf-str)) [:naninf (apply str (rest naninf-str))]])
      "+nan.0"
      "-nan.0"
      "+inf.0"
      "-inf.0")
    (are [naninf-failure] (failure? (parse naninf-failure))
      "nan.0"
      "inf.0"
      "nan"
      "inf")))

(deftest top-level-numbers
  (let [parse #(parse % :starting-at :number)]
    (is (= (parse "#b-1101")
           [:number
            [:prefix2 [:exactness] [:radix2 "#b"]]
            [:complex2 [:real2 [:sign "-"] [:ureal2 [:uinteger2 "1" "1" "0" "1"]]]]]))

    (is (= (parse "#e-1.5@.99d10|100")
           [:number
            [:prefix10 [:exactness "#e"] [:radix10]]
            [:complex10
             [:real10 [:sign "-"] [:ureal10 [:decimal10 "1" "." "5" [:suffix]] [:mantissa-width]]]
             "@"
             [:real10
              [:sign]
              [:ureal10
               [:decimal10 "." "9" "9" [:suffix [:exponent-marker "d"] [:sign] "1" "0"]]
               [:mantissa-width "|" "1" "0" "0"]]]]]))

    (is (= (parse "1/5+1i")
           [:number
            [:prefix10 [:exactness] [:radix10]]
            [:complex10
             [:real10 [:sign] [:ureal10 [:uinteger10 "1"] "/" [:uinteger10 "5"]]]
             "+"
             [:ureal10 [:uinteger10 "1"]]
             "i"]]))

    (is (= (parse "1-inf.0i")
           [:number
            [:prefix10 [:exactness] [:radix10]]
            [:complex10
             [:real10 [:sign] [:ureal10 [:uinteger10 "1"]]]
             "-"
             [:naninf "inf.0"]
             "i"]]))

    (is (= (parse "-9.999i")
           [:number
            [:prefix10 [:exactness] [:radix10]]
            [:complex10
             "-"
             [:ureal10 [:decimal10 "9" "." "9" "9" "9" [:suffix]] [:mantissa-width]]
             "i"]]))

    (is (= (parse "+nan.0i")
           [:number
            [:prefix10 [:exactness] [:radix10]]
            [:complex10
             "+"
             [:naninf "nan.0"]
             "i"]]))

    (is (= (parse "-i")
           [:number
            [:prefix10 [:exactness] [:radix10]]
            [:complex10
             "-"
             "i"]]))))

(deftest numbers
  (uinteger2)
  (uinteger8)
  (uinteger10)
  (uinteger16)
  (number-prefixes)
  (number-suffixes)
  (decimal10)
  (ureals)
  (naninf)
  (top-level-numbers))

(defn test-ns-hook []
  (comment-successes)
  (comment-failures)
  (numbers))