package com.mateusz.onthisday.eventlist


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mateusz.onthisday.data.remote.responses.Selected
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EventListScreen(viewModel: EventListViewModel) {
    //val events = viewModel.eventList.value
    val eventList by remember { mutableStateOf(viewModel.eventList) }
    val isLoading by remember { viewModel.isLoading }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    val dateDialogState = rememberMaterialDialogState()

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
                    .size(100.dp)
                    .padding(16.dp)
                    .animateContentSize(),
                color = MaterialTheme.colorScheme.primary // Możesz dostosować kolor
            )
        } else {


            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        currentDate = currentDate.minusDays(1)

                        viewModel.reloadEventsByDate(currentDate)
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous day"
                        )
                    }

                    IconButton(onClick = {
                        currentDate = currentDate.plusDays(1)


                        viewModel.reloadEventsByDate(currentDate)
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next day"
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable {

                            dateDialogState.show()

                        },
                    contentAlignment = Alignment.Center

                ) {
                    // Display the current date
                    // You can customize the formatting based on your needs
                    // For example, you might want to use a Text() composable instead
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Display the formatted date
                        // Using Text() composable for simplicity, you can replace it with your UI elements



                        Text(text = formattedDate(currentDate))
                    }
                }
            }

            MaterialDialog (
                dialogState = dateDialogState,
                buttons = {

                    positiveButton(text = "Ok"){

                    }
                    negativeButton(text = "Cancel")

                } ,
            ){
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date",

                ){
                    currentDate = it
                    viewModel.reloadEventsByDate(currentDate)
                }
            }

            // Wyświetlanie listy zdarzeń
            LazyColumn {
                items(eventList.value.data?.selected ?: emptyList()) { event ->
                    when(event){
                        is Selected ->{
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                            ) {
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
}

@Composable
fun formattedDate(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("d MMM").format(date)
}

