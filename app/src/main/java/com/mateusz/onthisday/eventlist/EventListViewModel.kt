package com.mateusz.onthisday.eventlist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateusz.onthisday.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val repository: EventRepository
): ViewModel() {


    var isLoading = mutableStateOf(false)


  var eventList = mutableStateOf<List<Any?>>(emptyList())

    init {
        loadEventsPaginated()
    }

    fun loadEventsPaginated(){
        viewModelScope.launch {

            isLoading.value = true
            try {
                //val result = repository.getEventList("11", "26",10,10)
                val result = repository.getEventListOfType("selected","11", "26")

                Log.d("result", result.data.toString())

                eventList.value = listOf(result)
                Log.d("Test", eventList.value.get(1).toString())
            }catch (e: Exception){
                Log.d("Error", e.toString())
            }
        }
    }
}