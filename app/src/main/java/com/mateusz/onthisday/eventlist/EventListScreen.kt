package com.mateusz.onthisday.eventlist


import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.mateusz.onthisday.data.remote.responses.Selected
import com.mateusz.onthisday.ui.theme.Grey10
import com.mateusz.onthisday.ui.theme.Grey20
import com.mateusz.onthisday.ui.theme.Grey90
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
            .background(Grey20),

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
                modifier = Modifier
                    .background(Grey10),
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

            Spacer(modifier = Modifier.height(8.dp))

            // Wyświetlanie listy zdarzeń
            LazyColumn(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                items(eventList.value.data?.selected ?: emptyList()) { event ->
                    when(event){
                        is Selected ->{
                            Column(
                                modifier = Modifier
                                    .background(Grey10, RoundedCornerShape(16.dp))
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {

                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(8.dp)
                                ){
                                    Text(
                                        text = event.year.toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        modifier = Modifier,
                                        text = event.pages[0].titles?.normalized.toString(),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(text = event.text.toString())
                                }


                                Spacer(modifier = Modifier.height(8.dp))


                                AsyncImage(
                                    model = event.pages[0].originalimage?.source.toString(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 16.dp))





                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
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

        }
    }
}

@Composable
fun formattedDate(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("d MMM").format(date)
}

