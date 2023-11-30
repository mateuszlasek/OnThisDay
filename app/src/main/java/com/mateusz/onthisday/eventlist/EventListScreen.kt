package com.mateusz.onthisday.eventlist


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mateusz.onthisday.data.remote.responses.Births
import com.mateusz.onthisday.data.remote.responses.Deaths
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
                            event.pages.forEach { page ->
                                when (page) {
                                  //  is Births -> BirthItem(page)
                                   // is Deaths -> DeathItem(page)
                                   // is Events -> EventItem(page)
                                    // Dodaj obsługę innych kategorii (np. Holiday, Selected) według potrzeb
                                    else -> UnknownEventItem()
                                }
                            }
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
fun BirthItem(birth: Births) {
    // Tutaj możesz dostosować, jak wyświetlasz informacje o urodzeniu
    Text(text = "Birth: ${birth.text}, Year: ${birth.year}")
}

@Composable
fun DeathItem(death: Deaths) {
    // Tutaj możesz dostosować, jak wyświetlasz informacje o śmierci
    Text(text = "Death: ${death.text}, Year: ${death.year}")
}

@Composable
fun EventItem(event: Events) {
    // Tutaj możesz dostosować, jak wyświetlasz informacje o wydarzeniu
    Text(text = "Event: ${event.text} Year: ${event.year}")
}

@Composable
fun UnknownEventItem() {
    Text(text = "Unknown Event")
}
