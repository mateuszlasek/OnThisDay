package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Births (

  @SerializedName("text"  ) var text  : String?          = null,
  @SerializedName("pages" ) var pages : ArrayList<Pages> = arrayListOf(),
  @SerializedName("year"  ) var year  : Int?             = null

)