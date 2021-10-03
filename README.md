# tempfiles

This is a small Clojure library for managing temporary files.

It allows to create a temporary files with a given extension (e.g., ".csv"), and get not only its path, but a suggested standard URL where it can be served relatively to a webserver.

By adopting this suggested URL, various tools such as Notespace can agree with various libraries such as Viz.clj on where temporary files should be placed.

For example, Viz.clj will create a temporary file with some data to be passed for visualization, Notespace will serve it on its webserver, and Viz.clj will create a Vega specification which knows what URL to use for reading the file.

## Usage


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
