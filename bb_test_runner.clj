(ns bb-test-runner
  (:require
   [clojure.test :as t]
   [mint.core-test]
   [mint.evaluator-test]
   [mint.evaluator.condition-test]
   [mint.evaluator.threading-test]))

(t/run-tests
 'mint.core-test
 'mint.evaluator-test
 'mint.evaluator.condition-test
 'mint.evaluator.threading-test)
