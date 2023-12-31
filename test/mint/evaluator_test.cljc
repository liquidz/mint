(ns mint.evaluator-test
  (:require
   [clojure.test :as t]
   [mint.evaluator :as sut]))

(t/deftest eval*-default-test
  (t/testing "replace value"
    (t/is (= 10 (sut/eval* 'foo {:foo 10}))))

  (t/testing "eval function"
    (t/is (= 10 (sut/eval* '(inc 9) {:inc inc})))
    (t/is (= 10 (sut/eval* '(inc (inc 8)) {:inc inc}))))

  (t/testing "qualified keyword"
    (t/is (= 10 (sut/eval* '(->> foo/bar) {:foo/bar 10}))))

  (t/testing "unknown value"
    (t/is (nil? (sut/eval* 'unknown {}))))

  (t/testing "unknown function"
    (t/is (nil? (sut/eval* '(unknown 1) {})))))

(t/deftest evaluate-test
  (t/is (= "" (sut/evaluate [] {})))

  (t/testing "string"
    (t/is (= "foo"
             (sut/evaluate [[:string "foo"]] {}))))

  (t/testing "expression"
    (t/is (= "bar"
             (sut/evaluate [[:expression "{{foo}}"]]
                           {:foo "bar"})))
    (t/is (= "bar"
             (sut/evaluate [[:expression "{{f}}"]
                            [:expression "{{oo}}"]]
                           {:f "b" :oo "ar"}))))

  (t/testing "string and expression"
    (t/is (= "hello world!"
             (sut/evaluate [[:string "hello "]
                            [:expression "{{foo}}"]
                            [:string "!"]]
                           {:foo "world"})))))
