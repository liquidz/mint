(ns mint.evaluator.condition-test
  (:require
   [clojure.test :as t]
   [mint.evaluator :as sut]
   [mint.evaluator.condition]))

(t/deftest eval*-condition-test
  (t/testing "when"
    (t/is (= "hello" (sut/eval* '(when foo "hello") {:foo 10})))
    (t/is (= "10hello" (sut/eval* '(when foo (str foo "hello")) {:foo 10
                                                                 :str str})))
    (t/is (nil? (sut/eval* '(when foo "hello") {:foo nil})))
    (t/is (nil? (sut/eval* '(when foo "hello") {:foo false})))
    (t/is (nil? (sut/eval* '(when foo "hello") {})))))
