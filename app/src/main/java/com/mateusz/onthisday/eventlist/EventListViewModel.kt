package com.mateusz.onthisday.eventlist

import android.util.Log
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val repository: EventRepository
): ViewModel() {

    private var curPage = 0

    var isLoading = mutableStateOf(false)
    var isSuccess = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var loadError = mutableStateOf("")
    var isError = mutableStateOf(false)

    var _eventList = MutableStateFlow<Resource<AllEvents>>(Resource.Loading())
    val eventList: StateFlow<Resource<AllEvents>> = _eventList


    init {
        reloadEventsByDate(LocalDate.now())
    }

    fun reloadEventsByDate(date: LocalDate) {

        val month = DateTimeFormatter.ofPattern("MM").format(date)
        val day = DateTimeFormatter.ofPattern("dd").format(date)


        viewModelScope.launch {
            isLoading.value = true
            isError.value = false
            val result = repository.getEventList(month, day)
            when(result){
                is Resource.Success ->{

                    _eventList.value = Resource.Success(result.data!!)
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    Log.d("Error: 2",loadError.value)
                    isLoading.value = false
                    isError.value = true

                }
                is Resource.Loading ->{

                }
            }
        }
    }



}