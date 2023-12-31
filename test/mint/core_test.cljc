(ns mint.core-test
  (:require
   [clojure.test :as t]
   [mint.core :as sut]
   #?@(:bb []
       :clj [[clojure.java.io :as io]
             testdoc.core])))

#?(:bb nil
   :clj (t/deftest README-test
          (t/is (testdoc (slurp (io/file "README.adoc"))))))

(t/deftest eval*-test
  (t/is (= 10 (sut/eval* 'foo {:foo 10})))
  (t/is (= 10 (sut/eval* '(inc 9) {:inc inc})))
  (t/is (= 10 (sut/eval* '(inc (inc 8)) {:inc inc})))

  (t/testing "thread first"
    (t/is (= 10 (sut/eval* '(-> 9 inc) {:inc inc})))
    (t/is (= 10 (sut/eval* '(-> foo (- 1)) {:foo 11, :- -})))
    (t/is (= 10 (sut/eval* '(-> (inc foo) (- 1)) {:foo 10, :- -, :inc inc}))))

  (t/testing "thread last"
    (t/is (= 10 (sut/eval* '(->> 9 inc) {:inc inc})))
    (t/is (= 10 (sut/eval* '(->> foo (- 11)) {:foo 1, :- -})))
    (t/is (= 10 (sut/eval* '(->> (inc foo) (- 11)) {:foo 0, :- -, :inc inc}))))

  (t/testing "qualified keyword"
    (t/is (= 10 (sut/eval* '(->> foo/bar) {:foo/bar 10})))))

(t/deftest render-test
  (t/is (= "" (sut/render "" {})))
  (t/is (= "foo" (sut/render "foo" {})))
  (t/is (= "foo" (sut/render "f{{-> x}}" {:x "oo"})))
  (t/is (= "foo" (sut/render "f{{-> x (str \"o\")}}" {:x "o" :str str}))))
