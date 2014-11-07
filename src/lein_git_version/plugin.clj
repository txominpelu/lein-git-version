(ns lein-git-version.plugin
  (:use
   clojure.pprint
   [leiningen.git-version :only [get-git-version get-git-rev]]))

(defn middleware
  [project]
  (let [code (str
              ";; Do not edit.  Generated by lein-git-version plugin.\n"
              "(ns " (:name project) ".version)\n"
              "(def version \"" (get-git-rev) "\")\n")
        proj-dir (.toLowerCase (.replace (:name project) \- \_))
        filename (if (:git-version-path project)
                   (str (:git-version-path project) "/version.clj")
                   (str (or (first (:source-paths project)) "src") "/"
                        proj-dir "/version.clj"))]
    (-> project
        (update-in [:injections] concat `[(spit ~filename ~code)])
        (assoc :version (get-git-version)))))
