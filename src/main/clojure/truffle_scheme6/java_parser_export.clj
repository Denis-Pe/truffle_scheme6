(ns truffle-scheme6.java-parser-export
  (:require [truffle-scheme6.reader :refer [read-scheme]])
  (:import (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.roots SchemeRoot)))

(defn parse
  [l s]
  (SchemeRoot. l (into-array SchemeNode (read-scheme s))))
