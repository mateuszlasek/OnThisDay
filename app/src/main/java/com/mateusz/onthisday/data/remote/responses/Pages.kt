package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Pages (

    @SerializedName("type"               ) var type              : String?      = null,
    @SerializedName("title"              ) var title             : String?      = null,
    @SerializedName("displaytitle"       ) var displaytitle      : String?      = null,
    @SerializedName("namespace"          ) var namespace         : Namespace?   = Namespace(),
    @SerializedName("wikibase_item"      ) var wikibaseItem      : String?      = null,
    @SerializedName("titles"             ) var titles            : Titles?      = Titles(),
    @SerializedName("pageid"             ) var pageid            : Int?         = null,
    @SerializedName("lang"               ) var lang              : String?      = null,
    @SerializedName("dir"                ) var dir               : String?      = null,
    @SerializedName("revision"           ) var revision          : String?      = null,
    @SerializedName("tid"                ) var tid               : String?      = null,
    @SerializedName("timestamp"          ) var timestamp         : String?      = null,
    @SerializedName("description"        ) var description       : String?      = null,
    @SerializedName("description_source" ) var descriptionSource : String?      = null,
    @SerializedName("content_urls"       ) var contentUrls       : ContentUrls? = ContentUrls(),
    @SerializedName("extract"            ) var extract           : String?      = null,
    @SerializedName("extract_html"       ) var extractHtml       : String?      = null,
    @SerializedName("normalizedtitle"    ) var normalizedtitle   : String?      = null

)