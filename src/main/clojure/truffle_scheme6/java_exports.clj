(ns truffle-scheme6.java-exports
  (:require [truffle-scheme6.parser-types :refer [specialize tagged to-java]]
            [truffle-scheme6.reader :refer [read-scheme]])
  (:import (com.oracle.truffle.api.frame FrameDescriptor)
           (com.oracle.truffle.api.source Source)
           (truffle_scheme6 SchemeNode)
           (truffle_scheme6.nodes.roots SchemeRoot)))

(defn parse
  [l ^Source s]
  (let [frame-desc-builder (FrameDescriptor/newBuilder)
        root-forms (->> s
                        (.getCharacters)
                        (read-scheme)
                        (map specialize)
                        (map #(tagged % {} frame-desc-builder [SchemeRoot/FRAME_NAME]))
                        (map to-java)
                        (into-array SchemeNode))
        built (.build frame-desc-builder)]
    (SchemeRoot. l built root-forms)))