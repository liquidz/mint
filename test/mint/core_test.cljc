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

(t/deftest render-test
  (t/is (= "" (sut/render "" {})))
  (t/is (= "foo" (sut/render "foo" {})))
  (t/is (= "foo" (sut/render "f{{-> x}}" {:x "oo"})))
  (t/is (= "foo" (sut/render "f{{-> x (str \"o\")}}" {:x "o" :str str}))))
