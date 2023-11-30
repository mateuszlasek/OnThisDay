package com.mateusz.onthisday.data.remote.responses

data class Birth(
    val pages: List<Page>,
    val text: String,
    val year: Int
)