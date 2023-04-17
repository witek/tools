#!/usr/bin/env bb

(ns gtsay
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.tools.cli :as cli]

   [babashka.http-client :as http]
   [babashka.process :as process]))

;;; url

(defn url [text]
  (let [url (str "https://translate.google.com/translate_tts?ie=UTF-8&tl=en-US&client=tw-ob"
                 "&q=" (java.net.URLEncoder/encode text))]
    url))

(comment
  (url "build failed"))

;;; download

(defn download [url file-path]
  (let [response (http/get url {:as :stream})
        file (io/file file-path)]
    (io/copy (-> response :body) file)
    file))

(comment
  (download (url "hello") "/home/witek/inbox/test.mp3"))

;;; play

(defn play [file-path]
  (process/shell "/usr/bin/play" "-q" file-path))

(comment
  (play "/home/witek/inbox/test.mp3"))

;;; combine

(defn say [text]
  (let [file-path (-> (java.io.File/createTempFile "gtsay_" ".mp3")
                      .toString)
        url (url text)
        ]
    (try
      (download url file-path)
      (play file-path)
      (finally
        (let [file (io/file file-path)]
          (when (-> file .exists)
            (-> file .delete)))))

    )
  )

(comment
  (say "hello isabella"))

;;; cli

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(def opts (cli/parse-opts *command-line-args* cli-options))
(def arguments (-> opts :arguments seq))

(comment
  (def arguments ["hello" "world"]))

(def text (->> arguments (str/join " ")))

(when-not (str/blank? text)
  (say text))
