(ns ^{:author "Daniel Habib Vieira da Silva", :doc "My CV template"} nerd-cv.core
  (:require
   [clj-pdf.core :as pdf]
   [clj-pdf.graphics-2d :as g]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defonce fonts (g/g2d-register-fonts [[".fonts/", true]]))

;; colors
(def sidebar-background [0 0 128])
(def white [255 255 255])
(def sidebar-text-color (map #(- % 25) [255 255 255]))
(def content-text-color (map #(+ % 25) [0 0 0]))

(def left-margin 10)
(def right-margin 10)
(def top-margin 10)
(def bottom-margin 10)


(defn- spacers-2
  []
  [[:pdf-cell {}
    [:spacer 1]]])

(defn- spacers
  []
  [[:pdf-cell {:set-border [:top] :border-color sidebar-background}
    [:spacer 1]]])

(defn- parse-concact [[k v]]
  (let [target (:target v)] ; external link
    (if target
      [:paragraph [:anchor {:styles [:underline] :target target}
                   (str (name k) ": " (:label v))]]
      [:phrase {} (str "   " (name k) ": " v)])))

(defn- top-contacts
  [k v]
  [[:pdf-cell {:set-border [:top] :border-color sidebar-background}
    [:phrase [:list
              [:heading {:style {:size 10 :color sidebar-text-color}} (str k ": ")]
              (let [target (:target v)] ; external link
                (if target
                  [:paragraph
                   [:anchor {:color sidebar-text-color :styles [:underline] :target target} (:label v)]]
                  [:phrase {:color sidebar-text-color} v]))]]]])

(defn- sidebar-contacts
  [k v]
  (into []
        [[:pdf-cell {:set-border [:top] :border-color sidebar-background}
          [:phrase [:list
                    [:heading {:style {:size 10 :color sidebar-text-color}} (str k ": ")]
                    (let [target (:target v)] ; external link
                      (if target
                        [:paragraph
                         [:anchor {:color sidebar-text-color :styles [:underline] :target target} (:label v)]]
                        [:phrase {:color sidebar-text-color} v]))]]]]))

(defn- skill-set
  [v]
  [[:pdf-cell {:set-border [:top] :border-color sidebar-background}
    [:phrase
     [:list
      [:phrase {:color sidebar-text-color} v]]]]])

(defn- sidebar-skills
  [v]
  [[:pdf-cell {:set-border [:top] :border-color sidebar-background}
    [:phrase
     [:list
      [:phrase {:color sidebar-text-color} v]]]]])

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

(defn- experience-section
  [project]
  (into []
        [[:pdf-cell {:set-border []}
          [:list {:symbol ""}
           [:spacer 1]
           [:paragraph {:size 14 :color content-text-color} (:company project)]
           [:heading {:style {:size 13 :color content-text-color}} (str/join " | " (:titles project))]
           [:phrase (format-summary (:description project))]]]]))

(defn- chunk-title [text]
  [:chunk {:size 18 :styles [:bold :underline]} (str text "\n")])

(defn create-cv-with-picture
  [{:keys [contact] :as cv} profile-picture cv-filename]
  (let [doc [{:register-system-fonts? true
              :font {:size 12 :encoding :unicode :ttf-name ".fonts/garamond/EBGaramond-Medium.ttf"}
              :left-margin left-margin
              :right-margin right-margin
              :top-margin top-margin
              :bottom-margin bottom-margin}
             [:pdf-table {:horizontal-align :center
                          :set-border []
                          :width-percent 100}
              [30 70]
              [(let [base [:pdf-table {:set-border [], :background-color sidebar-background} [1]
                           [[:pdf-cell {:set-border [:top] :border-color sidebar-background} [:image {:xscale 0.25 :yscale 0.25 :align :center} profile-picture]]]
                           [[:pdf-cell {:set-border [:top] :border-color sidebar-background} [:heading {:style {:align :center :size 20 :color sidebar-text-color}} (:name cv)]]]
                           [[:pdf-cell {:set-border [:top] :border-color sidebar-background} [:heading {:style {:align :center :size 14 :color sidebar-text-color}} "Software Engineer"]]]]]
                 (-> base
                     (into [[[:pdf-cell {:set-border [:top] :border-color sidebar-background} [:spacer 2]]]])
                     (into [[[:pdf-cell {:set-border [:top] :border-color sidebar-background} [:heading {:style {:size 12 :color sidebar-text-color}} "Contact"]]]])
                     (into (for [contact-kw (keys contact)
                                 :let [contact (contact-kw contact)]]
                             (sidebar-contacts (name contact-kw) contact)))
                     (into (for [_n (range 2)]
                             (spacers)))
                     (into [[[:pdf-cell {:set-border [:top] :border-color sidebar-background} [:heading {:style {:size 12 :color sidebar-text-color}} "Skills"]]]])
                     (into (for [skill (:skills cv)]
                             (skill-set skill)))
                     (into (for [_n (range 22)]
                             (spacers)))))
               (let [base [:pdf-table {:width-percent 100} [1]
                           [[:pdf-cell {:set-border []}
                             [:paragraph (chunk-title "Summary") (format-summary (:summary cv))]]]]]
                 (-> base
                     (into [[[:pdf-cell {:set-border []}
                              [:paragraph (chunk-title "Experience")]]]])
                     (into (for [project (:projects cv)]
                             (experience-section project)))
                     (into [[[:pdf-cell {:set-border []}
                              [:paragraph (chunk-title "Education")]]]])
                     (into (for [education (:educations cv)]
                             (education-section education)))))]]]]

    (pdf/pdf doc cv-filename)
    cv-filename))

(do (defn create-cv
      [{:keys [contact] :as cv} cv-filename]
      (let [doc [{:register-system-fonts? true
                  :font {:size 12 :encoding :unicode :ttf-name ".fonts/garamond/EBGaramond-Medium.ttf"}
                  :left-margin left-margin
                  :right-margin right-margin
                  :top-margin top-margin
                  :bottom-margin bottom-margin}
                 [:pdf-table {:horizontal-align :center
                              :set-border []
                              :width-percent 100}
                  [1]
                  [(let [base [:pdf-table {:set-border [], :background-color white} [1]
                               [[:pdf-cell {:set-border [:top] :border-color white} [:heading {:style {:align :center :size 20 :color content-text-color}} (:name cv)]]]
                               [[:pdf-cell {:set-border [:top] :border-color white} [:heading {:style {:align :center :size 14 :color content-text-color}} "Software Engineer"]]]]]
                     (-> base
                         (into (for [_n (range 1)] (spacers-2)))
                         (into [[[:pdf-cell {:align :center :set-border [:top] :border-color sidebar-background}
                                  (reduce conj [:phrase {:horizontal-align :center}]
                                          (mapv parse-concact contact)
                                          #_#_#_[:anchor {:styles [:underline] :target "target"} "aaaa"]
                                              [:chunk {:styles [:bold :underline]} "bbbbbb"]
                                            [[:anchor {:styles [:underline] :target "target"} "aaaa"]])
                                  #_(into [:phrase "lol"]
                                          (mapv (fn [v] [:chunk {} v])
                                                (keys contact)))]]]
                               #_(for [contact-kw (keys contact)
                                       :let [contact (contact-kw contact)]]
                                   (top-contacts (name contact-kw) contact)))

                         (into (for [_n (range 2)] (spacers-2)))))
                   #_(let [base [:pdf-table {:width-percent 100} [1]
                                 [[:pdf-cell {:set-border []}
                                   [:paragraph (chunk-title "Summary") (format-summary (:summary cv))]]]]]
                       (-> base
                           (into [[[:pdf-cell {:set-border []}
                                    [:paragraph (chunk-title "Experience")]]]])
                           (into (for [project (:projects cv)]
                                   (experience-section project)))
                           (into [[[:pdf-cell {:set-border []}
                                    [:paragraph (chunk-title "Education")]]]])
                           (into (for [education (:educations cv)]
                                   (education-section education)))))]]]]

        (pdf/pdf doc cv-filename)
        cv-filename))
    (-main "./resources/sample.edn" nil "./cv.pdf"))

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

(defn -main
  [& [data picture output-file-path]]
  (if-not data
    (println "Usage: need to pass the edn path and the profile picture path.\nExample: clj -M:runner ~/Downloads/cv.edn ~/Downloads/profile.png")
    (let [resume-file (or output-file-path "cv.pdf")
          edn-data (load-edn data)]
      (if picture
        (let [cv (-> edn-data
                     (create-cv-with-picture picture resume-file))]
          (println cv))
        (let [cv (-> edn-data
                     (create-cv resume-file))]
          (println cv)))
      :ok)))

(comment
  (-main "./resources/sample.edn" "./resources/profile.jpg" "./cv.pdf")
  (-main "./resources/sample.edn" nil "./cv.pdf"))


