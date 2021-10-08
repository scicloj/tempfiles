(ns scicloj.tempfiles.api
  (:require [babashka.fs :as fs]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:import [java.io File]
           [java.nio.file Files Path Paths]
           [java.nio.file.attribute FileAttribute]))

(def ^:private *session-tempdir (atom  nil))

(def root
  "The root directory to hold all temporary directories
  as subdirectories"
  "/tmp/scicloj-files/")

(defn- make-new-tempdir! []
  (when-not (fs/exists? root)
    (fs/create-dirs root))
  (->> (Files/createTempDirectory
        (Paths/get root (into-array String []))
        "session-dir-"
        (into-array FileAttribute []))
       (reset! *session-tempdir)))

(defn session-tempdir!
  "Get the temporary directory of the current session."
  []
  (-> @*session-tempdir
      (or (make-new-tempdir!))
      str))

(defn tempfile!
  "Get the recommended `path` and `route` for a temporary file
  of the given extension, implicitly creating a temporary directory
  for this session, if needed."
  [extension]
  (let [file ^File (File/createTempFile "file-"
                                   extension
                                   (io/file (session-tempdir!)))
        path (.getPath file)
        [dir filename] (-> path
                           (string/split #"/"))]
    {:path  path
     :route (-> path
                (string/replace #"^/tmp" ""))}))

(defn- delete-tree-if-exists! [path]
  (when (fs/exists? path)
    (-> path
        io/file
        fs/delete-tree)))

(defn- delete-session-tempdir! []
  (some-> @*session-tempdir
          str
          delete-tree-if-exists!))

(defn- delete-all-tempdirs! []
  (-> root
      delete-tree-if-exists!))

(defn cleanup-session-tempdir! []
  "Remove and forget the temporary directory of this session.
  Use this to restart your session in Clojure component systems
  (component/mount/integrant/etc.)." 
  (delete-session-tempdir!)
  (reset! *session-tempdir nil))

(defn cleanup-all-tempdirs! []
  "Remove and forget all temporary directory of all sessions."
  (delete-all-tempdirs!)
  (reset! *session-tempdir nil))

(defn print-tempdirs-tree! []
  "Print the tree of temporary directories
 (requires a unix shell with the `tree` utility installed)."
  (-> (clojure.java.shell/sh "tree" root)
      :out
      println))
