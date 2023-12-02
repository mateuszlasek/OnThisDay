package com.mateusz.onthisday.data.remote.responses

import com.google.gson.annotations.SerializedName


data class AllEvents (

    @SerializedName("selected" ) var selected : ArrayList<Selected> = arrayListOf(),
    @SerializedName("births"   ) var births   : ArrayList<Births>   = arrayListOf(),
    @SerializedName("deaths"   ) var deaths   : ArrayList<Deaths>   = arrayListOf(),
    @SerializedName("events"   ) var events   : ArrayList<Events>   = arrayListOf(),
    @SerializedName("holidays" ) var holidays : ArrayList<Holidays> = arrayListOf()

)