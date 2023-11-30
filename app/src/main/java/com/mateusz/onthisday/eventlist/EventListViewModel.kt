package com.mateusz.onthisday.eventlist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateusz.onthisday.data.remote.responses.Events
import com.mateusz.onthisday.repository.EventRepository
import com.mateusz.onthisday.util.Constants.PAGE_SIZE
import com.mateusz.onthisday.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val repository: EventRepository
): ViewModel() {


    var isLoading = mutableStateOf(false)


  var cachedEventList = mutableStateOf<List<Any?>>(emptyList())

    init {
        loadEventsPaginated()
    }

    fun loadEventsPaginated(){
        viewModelScope.launch {

            isLoading.value = true
            try {
                Log.d("Log result", "przed result")
                val result = repository.getEventListOfType("selected","11", "26")
                Log.d("result", result.message.toString())
                Log.d("Log result", "po result")

                cachedEventList.value = listOf(result)
                Log.d("Test", cachedEventList.value.get(1).toString())
            }catch (e: Exception){
                Log.d("Error", e.toString())
            }
        }
    }
}