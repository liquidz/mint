(ns mint.evaluator.threading
  (:require
   [mint.evaluator :as evaluator]))

(defmethod evaluator/eval* '->
  [[_ v & forms] data]
  (reduce
   (fn [accm form]
     (let [[head & rest-form] (if (sequential? form) form [form])
           form' (concat (list head accm) rest-form)]
       (evaluator/eval* form' data)))
   (evaluator/eval* v data)
   forms))

(defmethod evaluator/eval* 'some->
  [[_ v & forms] data]
  (reduce
   (fn [accm form]
     (if (nil? accm)
       (reduced accm)
       (let [[head & rest-form] (if (sequential? form) form [form])
             form' (concat (list head accm) rest-form)]
         (evaluator/eval* form' data))))
   (evaluator/eval* v data)
   forms))

(defmethod evaluator/eval* '->>
  [[_ v & forms] data]
  (reduce
   (fn [accm form]
     (let [form' (concat (if (sequential? form) form [form])
                         [accm])]
       (evaluator/eval* form' data)))
   (evaluator/eval* v data)
   forms))

(defmethod evaluator/eval* 'some->>
  [[_ v & forms] data]
  (reduce
   (fn [accm form]
     (if (nil? accm)
       (reduced accm)
       (let [form' (concat (if (sequential? form) form [form])
                           [accm])]
         (evaluator/eval* form' data))))
   (evaluator/eval* v data)
   forms))
