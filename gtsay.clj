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
  (str "https://translate.google.com/translate_tts"
       "?ie=" "UTF-8"
       "&client=" "tw-ob"
       "&tl=" "en-US"
       "&q=" (java.net.URLEncoder/encode text)))

(comment
  (url "hello world"))

;;; download

(defn download [url file-path]
  (let [response (http/get url {:as :stream})
        file (io/file file-path)]
    (io/copy (-> response :body) file)
    file))

(comment
  (download (url "test") "/tmp/test.mp3"))

;;; play

(defn play-with- [& cmd]
  (when (-> cmd first io/file .exists)
    (let [result (apply process/shell cmd)]
      (-> result :exit (= 0)))))

(defn play [file-path]
  (or

   (play-with- "/usr/bin/play"
               "-q" file-path)

   (play-with- "/usr/bin/ffplay"
               "-v" "0"
               "-nodisp"
               "-autoexit"
               file-path)

   (play-with- "/usr/bin/vlc"
               "-q"
               "--intf" "dummy"
               "--play-and-exit"
               file-path)))

(comment
  (play "/tmp/test.mp3"))

;;; combine

(defn say [text]
  (let [file-path (-> (java.io.File/createTempFile "gtsay_" ".mp3")
                      .toString)
        url (url text)]
    (try
      (download url file-path)
      (play file-path)
      (finally
        (let [file (io/file file-path)]
          (when (-> file .exists)
            (-> file .delete)))))))

(comment
  (say "thank you borkdude"))

;;; cli

(def cli-options
  [])

(def opts (cli/parse-opts *command-line-args* cli-options))
(def arguments (-> opts :arguments seq))

(comment
  (def arguments ["hello" "world"]))

(def text (->> arguments (str/join " ")))

(when-not (str/blank? text)
  (say text))
