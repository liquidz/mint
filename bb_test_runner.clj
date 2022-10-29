(ns bb-test-runner
  (:require
   [clojure.test :as t]
   [thrempl.core-test]))

(t/run-tests 'thrempl.core-test)
