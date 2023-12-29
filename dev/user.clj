(ns dev.user
  (:require
   [clojure.tools.nrepl.server :as nrepl]
   [nerd-cv.core :as core]))

(defonce nrepl-server (nrepl/start-server))
(spit "./.nrepl-port" (:port nrepl-server))
