package com.mateusz.onthisday.data.remote

import com.mateusz.onthisday.data.remote.responses.AllEvents
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @GET("en/onthisday/all/{MM}/{DD}") //en = {language}
    suspend fun getEventList(
        @Path("MM") month: String,
        @Path("DD") day: String
    ): AllEvents

    @GET("en/onthisday/{type}/{MM}/{DD}")
    suspend fun getEventListOfType(
        @Path("type") type: String,
        @Path("MM") month: String,
        @Path("DD") day: String,
        //@Query("limit") limit: Int,
        //@Query("offset") offset: Int
    ): AllEvents
}
