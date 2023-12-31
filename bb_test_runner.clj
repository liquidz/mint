(ns bb-test-runner
  (:require
   [clojure.test :as t]
   [mint.core-test]))

(t/run-tests 'mint.core-test)
