(ns ch11-playsync.core
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!!
                     go chan buffer close! thread
                     alts! alts!! timeout]])
  (:gen-class))

(defn hot-dog-machine
  []
  (let [in (chan) out (chan)]
    (go (<! in)
        (>! out "hot dog"))
    [in out]))

(defn hot-dog-machine-v2
  [hot-dog-count]
  (let [in (chan) out (chan)]
    (go (loop [hc hot-dog-count]
          (if (> hc 0)
            (let [input (<! in)]
              (if (= 3 input)
                (do (>! out "hot dog")
                    (recur (dec hc)))
                (do (>! out "wilted lettuce")
                    (recur hc))))
            (do (close! in)
                (close! out)))))
    [in out]))

(defn get-hot-dog
  "always returns a hotdog"
  [machine input]
  (let [[in out] (machine)]
       (>!! in input)
       (<!! out)))

