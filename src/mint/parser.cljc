(ns mint.parser
  (:require
   [clojure.string :as str]
   [mint.constant :as const]))

(defn- parse*
  [template-str]
  (let [start-idx (str/index-of template-str const/start-delm)
        end-idx (and start-idx
                     (str/index-of template-str const/end-delm (inc start-idx)))]
    (if (and start-idx end-idx)
      (let [end-idx' (+ end-idx (count const/end-delm))
            pre (subs template-str 0 start-idx)
            body (subs template-str start-idx end-idx')
            post (subs template-str end-idx')]
        [[:string pre]
         [:expression body]
         [:string post]])
      [[:string template-str]])))

(defn parse
  [template-str]
  (->> (loop [result []
              template-str template-str]
         (let [[pre body post :as parsed] (parse* template-str)]
           (if (seq post)
             (recur (concat result [pre body]) (second post))
             (concat result parsed))))
       (remove #(= % [:string ""]))))
