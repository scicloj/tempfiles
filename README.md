# tempfiles

This is a small Clojure library for managing temporary files.

It allows to create a temporary files with a given extension (e.g., ".csv"), and get not only its `path`, but a suggested standard `route` where it can be served relatively to a web app.

By adopting these suggested `path` & `route`, various tools such as Notespace can agree with various libraries such as Viz.clj on where temporary files should be placed.

For example, Viz.clj will create a temporary file with some data to be passed for visualization, creating it at the suggested `path`; Notespace will serve it on its webserver at the suggested `route`, and Viz.clj will create a Vega specification which knows what `route` to use for reading the file, so that it can be viewed correctly in Notespace.

## Usage

```clj

;; Begin with a clean slate.
(cleanup-all-tempdirs!)

;; Get the suggested `path` and `route` for a temprorary CSV file,
;; implicitly creating a temporary directory and remembering it
;; for this session.
(tempfile! ".csv")
;; =>
;; {:path "/tmp/scicloj-files/session-dir-8895124953168565280/file-16811038101563878892.csv",
;;  :route "/scicloj-files/session-dir-8895124953168565280/file-16811038101563878892.csv"}

;; Get the suggested `path` and `route` for a temprorary JSON file,
;; implicitly using the same temporary directory.
(tempfile! ".json")
;; =>
;; {:path "/tmp/scicloj-files/session-dir-8895124953168565280/file-10934288615104587977.json",
;;  :route "/scicloj-files/session-dir-8895124953168565280/file-10934288615104587977.json"}

;; Explicitly ask what the session temporary directory is.
(session-tepmdir!)
;; =>
;; "/tmp/scicloj-files/session-dir-8895124953168565280"

;; Print the tree structure of all temporary directories
;; of all sessions that haven't been cleaned yet
;; (requires a unix shell with the `tree` utility installed).
(print-tempdirs-tree!)
;; /tmp/scicloj-files/
;; └── session-dir-8895124953168565280
;; ├── file-10934288615104587977.json
;; └── file-16811038101563878892.csv

;; 1 directory, 2 files

;; Remove this session's temporary directory and forget about it.
(cleanup-session-tempdir!)

;; Print again.
(print-tempdirs-tree!)
;; /tmp/scicloj-files/

;; 0 directories, 0 files

;; Ask for a temporary file again, implicitly creating a new temporary directory.
(tempfile! ".csv")
;; {:path "/tmp/scicloj-files/session-dir-11643613431195588103/file-4476361447723718604.csv",
;;  :route "/scicloj-files/session-dir-11643613431195588103/file-4476361447723718604.csv"}
```

## License

Copyright © 2021 Scicloj

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
