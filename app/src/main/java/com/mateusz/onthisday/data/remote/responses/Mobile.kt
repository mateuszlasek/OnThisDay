package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Mobile (

  @SerializedName("page"      ) var page      : String? = null,
  @SerializedName("revisions" ) var revisions : String? = null,
  @SerializedName("edit"      ) var edit      : String? = null,
  @SerializedName("talk"      ) var talk      : String? = null

)