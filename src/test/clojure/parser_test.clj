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
  (is (= (parse "#!r6rs") '("#!r6rs"))))

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
  (is (= (parse "#b#i" :starting-at :prefix2) [:prefix2 [:radix2 "#b"] [:exactness "#i"]]))
  (is (= (parse "#b" :starting-at :prefix2) [:prefix2 [:exactness] [:radix2 "#b"]]))
  (is (= (parse "#E" :starting-at :prefix10) [:prefix10 [:exactness "#E"] [:radix10]]))
  (is (= (parse "#E#d" :starting-at :prefix10) [:prefix10 [:exactness "#E"] [:radix10 "#d"]])))

(deftest number-suffixes
  (is (= (parse "" :starting-at :suffix) [:suffix]))
  (let [src "d+16"]
    (is (= (parse src :starting-at :suffix) [:suffix [:exponent-marker "d"] [:sign "+"] "1" "6"])))
  (is (failure? (parse "L" :starting-at :suffix))))

(deftest numbers
  (uinteger2)
  (uinteger8)
  (uinteger10)
  (uinteger16)
  (number-prefixes)
  (number-suffixes))

(defn test-ns-hook []
  (comment-successes)
  (comment-failures)
  (numbers))