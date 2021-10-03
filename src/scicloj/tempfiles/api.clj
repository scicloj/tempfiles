(ns scicloj.tempfiles.api
  (:require [babashka.fs :as fs]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:import [java.io File]
           [java.nio.file Files Path Paths]
           [java.nio.file.attribute FileAttribute]))

(set! *warn-on-reflection* true)

(def *current-tempdir (atom  nil))

(def root "/tmp/scicloj-files/")

(defn make-new-tempdir! []
  (when-not (fs/exists? root)
    (fs/create-dirs root))
  (->> (Files/createTempDirectory
        (Paths/get root (into-array String []))
        "session-dir-"
        (into-array FileAttribute []))
       (reset! *current-tempdir)))

(defn current-tepmdir! []
  (-> @*current-tempdir
      (or (make-new-tempdir!))
      str))

(defn tempfile! [extension]
  (let [file ^File (File/createTempFile "file-"
                                   extension
                                   (io/file (current-tepmdir!)))
        path (.getPath file)
        [dir filename] (-> path
                           (string/split #"/"))]
    {:path path
     :url (-> path
              (string/replace #"^/tmp" ""))}))



(defn- delete-tree-if-exists! [path]
  (when (fs/exists? path)
    (-> path
        io/file
        fs/delete-tree)))

(defn- delete-current-tempdir! []
  (some-> @*current-tempdir
          str
          delete-tree-if-exists!))

(defn- delete-all-tempdirs! []
  (-> root
      delete-tree-if-exists!))

(defn cleanup-current-tempdir! []
  (delete-current-tempdir!)
  (reset! *current-tempdir nil))

(defn cleanup-all-tempdirs! []
  (delete-all-tempdirs!)
  (reset! *current-tempdir nil))

(defn print-tempdirs-tree! []
  (-> (clojure.java.shell/sh "tree" root)
      :out
      println))

(comment
  (current-tepmdir!)
  (tempfile! ".csv")
  (cleanup-current-tempdir!)
  (delete-all-tempdirs!)
  (print-tempdirs-tree!))
