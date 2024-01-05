(ns ^{:author "Daniel Habib Vieira da Silva", :doc "My CV template"} nerd-cv.core
  (:require
   [clj-pdf.core :as pdf]
   [clj-pdf.graphics-2d :as g]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defonce fonts (g/g2d-register-fonts [[".fonts/", true]]))

(def debug? false)
(def debug-border {:border true :cell-border true})
(defn- table-style
  ([style]
   (merge style (when debug? debug-border)))
  ([]
   (table-style {})))

;; colors
(def sidebar-background [0 0 128])
(def sidebar-text-color (map #(- % 25) [255 255 255]))
(def content-text-color (map #(+ % 25) [0 0 0]))

(def left-margin 10)
(def right-margin 10)
(def top-margin 10)
(def bottom-margin 10)

(defn- sidebar-contacts
  [k v]
  [:phrase [:list
            [:heading {:style {:size 10 :color sidebar-text-color}} k]
            (let [target (:target v)] ;; external link
              (if target
                [:paragraph
                 [:anchor {:color sidebar-text-color :styles [:underline] :target target} (:label v)]]
                [:phrase {:color sidebar-text-color} v]))]])

(defn- sidebar-skills
  [v]
  [:phrase
   [:list
    [:phrase {:color sidebar-text-color} v]]])

(defn- sidebar
  [cv contact profile-picture]
  [:table (table-style {:background-color sidebar-background})
   [[:cell {:rowspan 1} [:image #_{:xscale 0.15 :yscale 0.15 :align :center} profile-picture]]]
   [[:cell {:rowspan 1 :align :center} [:heading {:style {:size 20 :color sidebar-text-color}} (:name cv)]]]
   [[:cell {:rowspan 1 :align :center}
     [:heading {:style {:size 14 :color sidebar-text-color}} "Software Engineer"]]]
   [[:cell {:align :left}
     [:heading {:style {:size 12 :color sidebar-text-color}} "Contact"]
     (into [:list {:symbol " "}] (for [contact-kw (keys contact)
                                       :let [contact (contact-kw contact)]]
                                   (sidebar-contacts (name contact-kw) contact)))]]
   [[:cell {:rowspan 1000, :align :left}
     [:heading {:style {:size 12 :color sidebar-text-color}} "Skills"]
     (into [:list {:symbol " "}] (for [skill (:skills cv)]
                                   (sidebar-skills skill)))]]])

(defn- paragraph [text]
  (str "    " text))

(defn- format-summary [summary]
  (str/join "\n" (map paragraph summary)))

(defn- education-section
  [{:keys [name company] :as _education}]
  (into []
        [[:pdf-cell {:set-border []}
          [:list {:symbol ""}
           [:paragraph {:style :bold, :color content-text-color} name]
           [:paragraph {:color content-text-color} company]]]]))

(defn- project-section
  [project]
  (into []
        [[:cell
          [:list {:symbol ""}
           [:heading {:style {:size 12 :color content-text-color}} (str "Project: " (:name project))]
           [:heading {:style {:size 10 :color content-text-color}} (str "Company: " (:company project))]
           [:phrase (format-summary (:description project))]
           [:phrase
            [:chunk {:style :bold} (str "Technologies: ")]
            [:chunk (format-summary [(str/join ", " (:tech project))])]]]]]))

(defn- content
  [{:keys [educations projects summary]}]
  [:table (table-style)
   [[:cell {:align :left} [:paragraph (format-summary summary)]]]
   [[:cell #_{:rowspan 200}
     (into [:table (table-style)]
           (for [education educations]
             (education-section education)))]]
   [[:cell #_{:rowspan 200}
     (into [:table (table-style)]
           (for [project projects]
             (project-section project)))]]])

(defn create-cv
  [{:keys [contact] :as cv} profile-picture cv-filename]
  (let [doc [{:register-system-fonts? true
              :font {:size 12 :encoding :unicode :ttf-name ".fonts/garamond/EBGaramond-Medium.ttf"}
              :left-margin left-margin
              :right-margin right-margin
              :top-margin top-margin
              :bottom-margin bottom-margin}
             [:pdf-table {:horizontal-align :center
                          #_#_:width-percent 100}
              [30 70]
              [[:pdf-table {:background-color sidebar-background} [1]
                [[:pdf-cell [:image {:xscale 0.15 :yscale 0.15 :align :center} profile-picture]]]
                [[:pdf-cell [:heading {:style {:size 20 :color sidebar-text-color}} (:name cv)]]]
                [[:pdf-cell [:heading {:style {:size 14 :color sidebar-text-color}} "Software Engineer"]]]
                [[:pdf-cell [:heading {:style {:size 12 :color sidebar-text-color}} "Contact"]]]
                #_(into [:list {:symbol " "}] (for [contact-kw (keys contact)
                                                    :let [contact (contact-kw contact)]]
                                                (sidebar-contacts (name contact-kw) contact)))

                ["dqjpodq"]
                ["dqjpodq"]
                ["dqjpodq"]
                ["dqjpodq"]]
               #_[:pdf-cell [:heading {:style {:size 20 :color sidebar-text-color}} (:name cv)]]
               (into [:pdf-table {} [1]
                      [[:pdf-cell {:set-border []}
                        [:paragraph [:chunk {:style :bold} "Summary:\n"] (format-summary (:summary cv))]]]
                      [[:pdf-cell {:set-border []}
                        [:paragraph [:chunk {:style :bold} "Education:\n"]]]]]
                     (for [education (:educations cv)]
                       (education-section education)))]]]]

    (pdf/pdf doc cv-filename)
    cv-filename))

(defn load-edn
  "Load edn from an io/reader source (filename or io/resource)."
  [source]
  (try
    (with-open [r (io/reader source)]
      (edn/read (java.io.PushbackReader. r)))

    (catch java.io.IOException e
      (printf "Couldn't open '%s': %s\n" source (.getMessage e)))
    (catch RuntimeException e
      (printf "Error parsing edn file '%s': %s\n" source (.getMessage e)))))

(defn -main [& [data picture]]
  (if-not (and  data picture)
    (println "Usage: need to pass the edn path and the profile picture path.\nExample: clj -M:runner ~/Downloads/cv.edn ~/Downloads/profile.png")
    (let [cv (-> data
                 load-edn
                 (create-cv picture "cv.pdf"))]
      (println cv))))

(comment
  (-main "./resources/sample.edn"
         "./resources/profile.jpg"))


