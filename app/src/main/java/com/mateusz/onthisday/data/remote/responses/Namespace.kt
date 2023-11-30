package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Namespace (

  @SerializedName("id"   ) var id   : Int?    = null,
  @SerializedName("text" ) var text : String? = null

)