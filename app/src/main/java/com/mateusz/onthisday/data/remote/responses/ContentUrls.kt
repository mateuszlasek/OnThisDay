package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class ContentUrls (

  @SerializedName("desktop" ) var desktop : Desktop? = Desktop(),
  @SerializedName("mobile"  ) var mobile  : Mobile?  = Mobile()

)