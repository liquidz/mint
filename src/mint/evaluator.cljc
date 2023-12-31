(ns mint.evaluator
  (:require
   [clojure.string :as str]
   [mint.constant :as const]))

(defmulti eval*
  (fn [sexp _data]
    (if (sequential? sexp)
      (first sexp)
      sexp)))

(defmethod eval* :default
  [form data]
  (let [[first-item :as form] (if (sequential? form) form [form])
        form-count (count form)]
    (cond
      (and (= 1 form-count)
           (not (symbol? first-item)))
      first-item

      (= 1 form-count)
      (get data (keyword first-item))

      :else
      (let [f (get data (keyword first-item))]
        (when (fn? f)
          (some->> (rest form)
                   (seq)
                   (map #(eval* % data))
                   (apply f)))))))

(defn- evaluate*
  [expression-str data]
  (-> expression-str
      (str/replace const/start-delm "(")
      (str/replace const/end-delm ")")
      (read-string)
      (eval* data)))

(defn evaluate
  [parsed data]
  (->> parsed
       (map #(if (= :expression (first %))
               (evaluate* (second %) data)
               (second %)))
       (str/join "")))
