package com.mateusz.onthisday.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName="favourites",
    indices = [
        Index("text", unique = true),
        Index("id", unique = true)
    ]
)
class Favourite(
    val text: String?,
    val title: String?,
    val originalimage: String?,
    val year: Int?,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)