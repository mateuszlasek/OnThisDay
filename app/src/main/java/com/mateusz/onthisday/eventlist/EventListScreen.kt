package com.mateusz.onthisday.eventlist


import android.graphics.Color
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.mateusz.onthisday.data.remote.responses.Birth
import com.mateusz.onthisday.data.remote.responses.Death
import com.mateusz.onthisday.data.remote.responses.Event
import com.mateusz.onthisday.data.remote.responses.Events

@Composable
fun EventListScreen(viewModel: EventListViewModel) {
    val events = viewModel.cachedEventList.value
    val isLoading = viewModel.isLoading.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            // Kółko ładowania z animacją
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp)
                    .animateContentSize(),
                color = MaterialTheme.colorScheme.primary // Możesz dostosować kolor
            )
        } else {
            // Wyświetlanie listy zdarzeń
            LazyColumn {
                items(events) { event ->
                    when (event) {
                        is Events -> {
                            // Obsługa klasy Events, np. wyświetlanie ogólnego nagłówka itp.
                            Text(text = "Events Header")
                            event.births.forEach { birth ->
                                BirthItem(birth)
                            }
                            event.deaths.forEach { death ->
                                DeathItem(death)
                            }
                            event.events.forEach { event ->
                                EventItem(event)
                            }
                            // Dodaj obsługę innych kategorii (np. Holiday, Selected) według potrzeb
                        }
                        else -> {
                            UnknownEventItem()
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun BirthItem(birth: Birth) {
    // Tutaj możesz dostosować, jak wyświetlasz informacje o urodzeniu
    Text(text = "Birth: ${birth.text}, Year: ${birth.year}")
}

@Composable
fun DeathItem(death: Death) {
    // Tutaj możesz dostosować, jak wyświetlasz informacje o śmierci
    Text(text = "Death: ${death.text}, Year: ${death.year}")
}

@Composable
fun EventItem(event: Event) {
    // Tutaj możesz dostosować, jak wyświetlasz informacje o wydarzeniu
    Text(text = "Event: ${event.text} Year: ${event.year}")
}

@Composable
fun UnknownEventItem() {
    Text(text = "Unknown Event")
}