(ns mint.evaluator.condition
  (:require
   [mint.evaluator :as evaluator]))

(defmethod evaluator/eval* 'when
  [[_ v & forms] data]
  (when (evaluator/eval* v data)
    (evaluator/eval* (last forms) data)))
