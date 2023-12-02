package com.mateusz.onthisday.repository

import com.mateusz.onthisday.data.remote.EventApi
import com.mateusz.onthisday.data.remote.responses.AllEvents
import com.mateusz.onthisday.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class EventRepository @Inject constructor(
    private val api: EventApi
){

    suspend fun getEventList(month: String, day: String, limit: Int, offset: Int): Resource<AllEvents> {
        return try {
            val response = api.getEventList(month, day, limit, offset)
            Resource.Success(response)
        } catch (e: Exception) {
            // Tutaj możesz także zalogować szczegóły błędu, aby uzyskać więcej informacji
            return Resource.Error("An error occurred: ${e.message}", null)
        }
    }                                                                       //limit: Int, offset: Int
    suspend fun getEventListOfType(type: String, month: String, day: String): Resource<AllEvents> {
        return try {

            val response = api.getEventListOfType(type, month, day) //, limit, offset
            Resource.Success(response)
        } catch (e: Exception){
            return Resource.Error("An error occured: ${e.message}", null)
        }

    }


}