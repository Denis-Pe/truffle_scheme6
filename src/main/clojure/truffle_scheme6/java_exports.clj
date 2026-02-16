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
        source-text (.getCharacters s)
        root-forms (->> source-text
                        (read-scheme)
                        (map specialize)
                        (map #(tagged % {} frame-desc-builder [SchemeRoot/FRAME_NAME]))
                        (map #(to-java % {:source s}))
                        (into-array SchemeNode))
        built (.build frame-desc-builder)]
    (doto (SchemeRoot. l built root-forms)
      (.setSourceSection (.createSection s 0 (.length source-text))))))