(ns truffle-scheme6.parser-types
  (:import (truffle_scheme6.nodes.atoms.bools SFalseLiteralNode STrueLiteralNode)))

(defprotocol PSchemeNode
  (to-java [this] "Transforms a given node to a Java object"))

(defrecord FalseLiteral []
  PSchemeNode
  (to-java [this]
    (SFalseLiteralNode.)))

(defrecord TrueLiteral []
  PSchemeNode
  (to-java [this]
    (STrueLiteralNode.)))