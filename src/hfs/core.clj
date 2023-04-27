(ns hfs.core
  (:require [hato.client]
            [hickory.core]
            [hickory.select]))

(def http-client-config {:connect-timeout 8000
                         :redirect-policy :always})
(def hfs-instagram-url "https://www.instagram.com/p/CrhatZFSY1V/")

(defn fetch-hfs-instagram-og-image-url []
  (let [client   (hato.client/build-http-client http-client-config)
        response (hato.client/get hfs-instagram-url {:http-client client})
        body     (:body response)
        document (-> body
                     hickory.core/parse
                     hickory.core/as-hickory)
        og-image (hickory.select/select
                  (hickory.select/and
                   (hickory.select/tag :meta)
                   (hickory.select/attr :property #(= % "og:image")))
                  document)
        url      (-> og-image first :attrs :content)]
    url))

(comment
  (fetch-hfs-instagram-og-image-url)
  ;; => "https://scontent-ssn1-1.cdninstagram.com/v/t51.29350-15/343486930_3408957096022302_698909991979403961_n.webp?stp=c288.0.864.864a_dst-jpg_s640x640&_nc_cat=105&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=ZkbZ83h_bBwAX8bSO2Y&_nc_ht=scontent-ssn1-1.cdninstagram.com&oh=00_AfC6bihR0NP3ATsV9sH0jh4sUWcJXctEoRRQPAkxXWWakw&oe=64505E58"

  :rfc)

