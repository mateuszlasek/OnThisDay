package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Coordinates (

  @SerializedName("lat" ) var lat : Double? = null,
  @SerializedName("lon" ) var lon : Double? = null

)