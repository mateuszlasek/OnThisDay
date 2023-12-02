package com.mateusz.onthisday.eventlist


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mateusz.onthisday.data.remote.responses.Births
import com.mateusz.onthisday.data.remote.responses.Deaths
import com.mateusz.onthisday.data.remote.responses.Events
import com.mateusz.onthisday.data.remote.responses.Selected

@Composable
fun EventListScreen(viewModel: EventListViewModel) {
    //val events = viewModel.eventList.value
    val eventList by remember { mutableStateOf(viewModel.eventList) }
    val isLoading by remember { viewModel.isLoading }

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
                items(eventList.value.data?.selected ?: emptyList()) { event ->
                    when(event){
                        is Selected ->{
                            Text(text = event.pages[0].titles?.normalized.toString())
                            Text(text = event.year.toString())
                            Text(text = event.text.toString())
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
