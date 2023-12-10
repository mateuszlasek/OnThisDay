package com.mateusz.onthisday.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mateusz.onthisday.data.remote.responses.Pages

@Entity(tableName="favourites")
class Favourite(
    val text: String?,
    val pages: ArrayList<Pages>,
    val year: Int?,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)