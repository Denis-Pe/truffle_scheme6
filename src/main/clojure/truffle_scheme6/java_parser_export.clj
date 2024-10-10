(ns truffle-scheme6.java-parser-export
  (:require [truffle-scheme6.reader :refer [read-scheme]]
            [truffle-scheme6.parser-types :refer [specialize tagged to-java]])
  (:import (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.roots SchemeRoot)))

(defn probe
  [x]
  (prn x)
  x)

(defn parse
  [l s]
  (->> s
       (read-scheme)
       (map specialize)
       (map #(tagged % {})) ; root forms, start out with an empty map
       (map to-java)
       (into-array SchemeNode)
       (SchemeRoot. l)))