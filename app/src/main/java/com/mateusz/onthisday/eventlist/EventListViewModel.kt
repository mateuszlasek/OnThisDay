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



    var isLoading = mutableStateOf(false)
    var isSuccess = mutableStateOf(false)


    var _eventList = MutableStateFlow<Resource<AllEvents>>(Resource.Loading())
    val eventList: StateFlow<Resource<AllEvents>> = _eventList
    init {

        reloadEventsByDate(LocalDate.now(), "selected")
    }

    fun reloadEventsByDate(date: LocalDate, type: String) {

        val month = DateTimeFormatter.ofPattern("MM").format(date)
        val day = DateTimeFormatter.ofPattern("dd").format(date)


        viewModelScope.launch {
            isLoading.value = true
            try {
                val result = repository.getEventListOfType(type, month, day)
                _eventList.value = Resource.Success(result.data!!)
                isLoading.value = false
            } catch (e: Exception) {
                Log.d("Error", e.toString())
            }
        }
    }
}

