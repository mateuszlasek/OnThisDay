package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Thumbnail (

  @SerializedName("source" ) var source : String? = null,
  @SerializedName("width"  ) var width  : Int?    = null,
  @SerializedName("height" ) var height : Int?    = null

)