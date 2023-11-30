package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Titles (

  @SerializedName("canonical"  ) var canonical  : String? = null,
  @SerializedName("normalized" ) var normalized : String? = null,
  @SerializedName("display"    ) var display    : String? = null

)