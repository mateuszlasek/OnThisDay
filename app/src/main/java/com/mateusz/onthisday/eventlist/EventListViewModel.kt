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
import java.util.Calendar
import java.util.Date
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

        reloadEventsByDate(Calendar.getInstance().time)
    }

    fun reloadEventsByDate(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))

        viewModelScope.launch {
            isLoading.value = true
            try {
                val result = repository.getEventListOfType("selected", month, day)
                _eventList.value = Resource.Success(result.data!!)
                isLoading.value = false
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }
    }
}

