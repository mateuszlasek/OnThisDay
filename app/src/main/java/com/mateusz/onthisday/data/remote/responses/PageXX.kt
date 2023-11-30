package com.mateusz.onthisday.data.remote.responses

data class PageXX(
    val content_urls: ContentUrlsXX,
    val coordinates: Coordinates,
    val description: String,
    val description_source: String,
    val dir: String,
    val displaytitle: String,
    val extract: String,
    val extract_html: String,
    val lang: String,
    val namespace: Namespace,
    val normalizedtitle: String,
    val originalimage: Originalimage,
    val pageid: Int,
    val revision: String,
    val thumbnail: Thumbnail,
    val tid: String,
    val timestamp: String,
    val title: String,
    val titles: Titles,
    val type: String,
    val wikibase_item: String
)