package com.mateusz.onthisday.eventlist

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateusz.onthisday.data.remote.responses.AllEvents
import com.mateusz.onthisday.repository.EventRepository
import com.mateusz.onthisday.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val repository: EventRepository
): ViewModel() {



    var isLoading = mutableStateOf(false)
    var isSuccess = mutableStateOf(false)


    var _eventList = MutableStateFlow<Resource<AllEvents>>(Resource.Loading())
    val eventList: StateFlow<Resource<AllEvents>> = _eventList
    init {
        loadEventsPaginated()
    }

    private fun loadEventsPaginated(){
        viewModelScope.launch {

            isLoading.value = true

            try {
                //val result = repository.getEventList("11", "26",10,10)
                val result = repository.getEventListOfType("selected","11", "26")
                _eventList.value = Resource.Success(result.data!!)
                isLoading.value = false
                //Log.d("result", result.data.toString())




            }catch (e: Exception){
                Log.d("Error", e.toString())
            }
        }
    }
}