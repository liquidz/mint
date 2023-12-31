(ns mint.core
  (:require
   [mint.evaluator :as evaluator]
   [mint.evaluator.condition]
   [mint.evaluator.threading]
   [mint.parser :as parser]))

(defn render
  [template-str data]
  (-> template-str
      (parser/parse)
      (evaluator/evaluate data)))
