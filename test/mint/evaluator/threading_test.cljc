(ns mint.evaluator.threading-test
  (:require
   [clojure.test :as t]
   [mint.evaluator :as sut]
   [mint.evaluator.threading]))

(t/deftest eval*-threading-test
  (t/testing "->"
    (t/is (= 10 (sut/eval* '(-> 9 inc) {:inc inc})))
    (t/is (= 10 (sut/eval* '(-> foo (- 1)) {:foo 11, :- -})))
    (t/is (= 10 (sut/eval* '(-> (inc foo) (- 1)) {:foo 10, :- -, :inc inc}))))

  (t/testing "some->"
    (t/is (= 10 (sut/eval* '(some-> 9 inc) {:inc inc})))
    (t/is (nil? (sut/eval* '(some-> nil inc) {:inc inc})))
    (t/is (nil? (sut/eval* '(some-> 9 null inc) {:inc inc, :null (constantly nil)}))))

  (t/testing "->>"
    (t/is (= 10 (sut/eval* '(->> 9 inc) {:inc inc})))
    (t/is (= 10 (sut/eval* '(->> foo (- 11)) {:foo 1, :- -})))
    (t/is (= 10 (sut/eval* '(->> (inc foo) (- 11)) {:foo 0, :- -, :inc inc}))))

  (t/testing "some->>"
    (t/is (= 10 (sut/eval* '(some->> foo (- 11)) {:foo 1, :- -})))
    (t/is (nil? (sut/eval* '(some->> foo (- 11)) {:foo nil, :- -})))
    (t/is (nil? (sut/eval* '(some->> 9 null inc) {:inc inc :null (constantly nil)})))))
