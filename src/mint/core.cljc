(ns mint.core
  (:require
   [clojure.string :as str]))

(def ^:private start-delm "{{->")
(def ^:private end-delm "}}")
(def ^:private end-delm-count (count end-delm))

(defmulti eval*
  (fn [form _data]
    (when (sequential? form)
      (first form))))

(defmethod eval* :default
  [form data]
  (let [seq-form? (sequential? form)
        sym-form? (symbol? form)]
    (cond
      (and (not seq-form?)
           (not sym-form?))
      form

      (not seq-form?)
      (get data (keyword form))

      :else
      (when-let [f (get data (keyword (first form)))]
        (->> (rest form)
             (seq)
             (map #(eval* % data))
             (apply f))))))

(defmethod eval* '->
  [[_ v & forms] data]
  (when-let [x (eval* v data)]
    (reduce (fn [accm form]
              (let [[head & args] (if (sequential? form) form [form])
                    form' (concat (list head accm) args)]
                (eval* form' data)))
            x forms)))

(defmethod eval* '->>
  [[_ v & forms] data]
  (when-let [x (eval* v data)]
    (reduce (fn [accm form]
              (let [form' (concat (if (sequential? form) form [form])
                                  [accm])]
                (eval* form' data)))
            x forms)))

(defn- parse-pre
  [s]
  (if-let [idx (str/index-of s start-delm)]
    [(subs s 0 idx) (subs s idx)]
    [s nil]))

(defn- parse-post
  [s]
  (if-let [idx (str/index-of s end-delm)]
    [(subs s 0 (+ idx end-delm-count)) (subs s (+ idx end-delm-count))]
    [nil s]))

(defn- parse
  [s]
  (let [[pre tmp] (parse-pre s)
        [body post] (when tmp (parse-post tmp))]
    [pre body post]))

(defn- eval-body
  [body data]
  (let [form-str (-> body
                     (str/replace start-delm "(->")
                     (str/replace end-delm ")"))]
    (try
      (-> form-str
          (read-string)
          (eval* data)
          (str))
      (catch #?(:clj Exception :cljs js/Error) ex
        (throw (ex-info "Invalid form" {:form form-str
                                        :message (ex-message ex)}))))))

(defn render
  [s data]
  (loop [content s
         res []]
    (let [[pre body post] (parse content)]
      (if (and body post)
        (recur post (conj res pre (eval-body body data)))
        (str/join "" (conj res pre))))))
