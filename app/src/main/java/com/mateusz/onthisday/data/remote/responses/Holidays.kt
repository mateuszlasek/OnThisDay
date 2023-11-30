package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class Holidays (

  @SerializedName("text"  ) var text  : String?          = null,
  @SerializedName("pages" ) var pages : ArrayList<Pages> = arrayListOf()

)