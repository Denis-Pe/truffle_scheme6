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

(deftest positive-integers
  (are [src] (= (parse src)
                (list [:number
                       [:prefix10 [:exactness] [:radix10]]
                       [:complex10 [:real10 [:sign] [:ureal10 (reduce conj
                                                                      [:uinteger10]
                                                                      (map str src))]]]]))
    "0"
    "1"
    "123456789"))

(deftest negative-integers
  (are [src] (= (parse src)
                (list [:number
                       [:prefix10 [:exactness] [:radix10]]
                       [:complex10 [:real10 [:sign "-"] [:ureal10 (reduce conj
                                                                          [:uinteger10]
                                                                          (map str (rest src)))]]]]))
    "-0"
    "-1"
    "-123456789"))

(deftest numbers
  (positive-integers)
  (negative-integers))

(defn test-ns-hook []
  (comment-successes)
  (comment-failures)
  (numbers))