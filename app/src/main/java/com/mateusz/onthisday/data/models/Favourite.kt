package com.mateusz.onthisday.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mateusz.onthisday.data.remote.responses.Originalimage
import com.mateusz.onthisday.data.remote.responses.Pages
import org.json.JSONArray

@Entity(tableName="favourites")
class Favourite(
    val text: String?,
    val title: String?,
    val originalimage: String?,
    val year: Int?,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)