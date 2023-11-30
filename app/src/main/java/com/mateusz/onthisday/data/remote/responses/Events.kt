package com.mateusz.onthisday.data.remote.responses

data class Events(
    val births: List<Birth>,
    val deaths: List<Death>,
    val events: List<Event>,
    val holidays: List<Holiday>,
    val selected: List<Selected>
)