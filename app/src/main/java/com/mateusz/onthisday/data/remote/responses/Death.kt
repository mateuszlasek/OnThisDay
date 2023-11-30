package com.mateusz.onthisday.data.remote.responses

data class Death(
    val pages: List<PageX>,
    val text: String,
    val year: Int
)