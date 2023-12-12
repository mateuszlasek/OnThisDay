package com.mateusz.onthisday.eventlist


import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mateusz.onthisday.data.db.FavouriteViewModel
import com.mateusz.onthisday.data.models.Favourite
import com.mateusz.onthisday.data.remote.responses.AllEvents
import com.mateusz.onthisday.util.Resource
import com.mateusz.onthisday.util.TabItem
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventListScreen(
    viewModel: EventListViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    val eventList by remember { mutableStateOf(viewModel._eventList) }
    val isLoading by remember { viewModel.isLoading }
    val isError by remember {
        viewModel.isError
    }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var type by remember { mutableStateOf("") }



    val tabItems = listOf(
        TabItem(
            title = "Selected",
            type = "selected"
        ),
        TabItem(
            title = "Events",
            type = "events"
        ),
        TabItem(
            title = "Births",
            type = "births"
        ),
        TabItem(
            title = "Deaths",
            type = "deaths"
        ),
        TabItem(
            title = "Holidays",
            type = "holidays"
        )
    )

    val dateDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var selectedTabIndex by remember {
            mutableIntStateOf(0)
        }
        val pagerState = rememberPagerState {
            tabItems.size
        }
        LaunchedEffect(selectedTabIndex){
            pagerState.animateScrollToPage(selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress){
            if(!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                currentDate = currentDate.minusDays(1)



                viewModel.reloadEventsByDate(currentDate)

            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous day"
                )
            }

            Box(
                modifier = Modifier
                    .clickable {

                        dateDialogState.show()

                    },
                contentAlignment = Alignment.Center

            ) {
                Text(

                    fontSize = 20.sp,
                    text = formattedDate(currentDate)
                )
            }
            IconButton(
                onClick = {
                currentDate = currentDate.plusDays(1)


                viewModel.reloadEventsByDate(currentDate)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next day"
                )
            }
        }


        ScrollableTabRow(
            modifier = Modifier
                .padding(0.dp),
            containerColor =  MaterialTheme.colorScheme.background,
            edgePadding = 0.dp,
            selectedTabIndex = selectedTabIndex
        ) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(
                            text = tabItem.title,
                            softWrap = false,
                        )
                    }
                )
            }

        }

        if (isLoading) {
            // Kółko ładowania z animacją
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                        .animateContentSize(),
                    color = MaterialTheme.colorScheme.primary // Możesz dostosować kolor
                )

            }

        } else {


            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                contentAlignment = Alignment.Center
            ) {

                Column {

                    Spacer(modifier = Modifier.height(8.dp))
                    if(!isError){
                        //Horizontal Pager
                        HorizontalPager(
                            state = pagerState,
                        ) {index ->

                            type = tabItems[index].type

                            when (type) {
                                "selected" -> SelectedList(eventList = eventList, favouriteViewModel = favouriteViewModel)
                                "events" -> EventsList(eventList = eventList, favouriteViewModel = favouriteViewModel)
                                "births" -> BirthsList(eventList = eventList, favouriteViewModel = favouriteViewModel)
                                "deaths" -> DeathsList(eventList = eventList, favouriteViewModel = favouriteViewModel)
                                "holidays" -> HolidaysList(eventList = eventList, favouriteViewModel = favouriteViewModel)
                                else -> {
                                    emptyList<AllEvents>()
                                }
                            }

                        }
                    } else {
                        ErrorScreen(currentDate, viewModel)
                    }


                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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

@Composable
fun ErrorScreen(currentDate: LocalDate, viewModel: EventListViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Error", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.reloadEventsByDate(currentDate)
            }) {
                Text(text = "Refresh")
            }
        }
    }
}


@Composable
fun SelectedList(eventList: StateFlow<Resource<AllEvents>>, favouriteViewModel: FavouriteViewModel){
    val coroutineScope = rememberCoroutineScope()
    // Wyświetlanie listy zdarzeń
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
    ) {

        items(eventList.value.data?.selected ?: emptyList()) { event ->

            var isMenuExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {

                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = event.year.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopEnd)
                        ){
                            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More"
                                )
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Add to Favourites") },
                                    onClick = {

                                        val favEvent = Favourite(
                                            event.text,
                                            event.pages[0].titles?.normalized.toString(),
                                            event.pages[0].originalimage?.source.toString(),
                                            event.year,
                                            0
                                        )


                                        coroutineScope.launch {
                                            favouriteViewModel.addToFavourites(favEvent)
                                        }

                                        isMenuExpanded = false
                                    })
                            }

                        }

                    }

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
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
fun EventsList(eventList: StateFlow<Resource<AllEvents>>, favouriteViewModel: FavouriteViewModel){
    val coroutineScope = rememberCoroutineScope()

    // Wyświetlanie listy zdarzeń
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
    ) {

        items(eventList.value.data?.events ?: emptyList()) { event ->

            var isMenuExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {

                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = event.year.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopEnd)
                        ){
                            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More"
                                )
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Add to Favourites") },
                                    onClick = {val favEvent = Favourite(
                                        event.text,
                                        event.pages[0].titles?.normalized.toString(),
                                        event.pages[0].originalimage?.source.toString(),
                                        event.year,
                                        0
                                    )
                                        coroutineScope.launch {
                                            favouriteViewModel.addToFavourites(favEvent)
                                        }

                                        isMenuExpanded = false
                                    })
                            }

                        }

                    }
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
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BirthsList(eventList: StateFlow<Resource<AllEvents>>, favouriteViewModel: FavouriteViewModel){

    val coroutineScope = rememberCoroutineScope()

    // Wyświetlanie listy zdarzeń
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
    ) {

        items(eventList.value.data?.births ?: emptyList()) { event ->

            var isMenuExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {

                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = event.year.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopEnd)
                        ) {
                            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More"
                                )
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Add to Favourites") },
                                    onClick = {
                                        val favEvent = Favourite(
                                            event.text,
                                            event.pages[0].titles?.normalized.toString(),
                                            event.pages[0].originalimage?.source.toString(),
                                            event.year,
                                            0
                                        )
                                        coroutineScope.launch {
                                            favouriteViewModel.addToFavourites(favEvent)
                                        }

                                        isMenuExpanded = false
                                    })
                            }

                        }

                    }
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
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DeathsList(eventList: StateFlow<Resource<AllEvents>>, favouriteViewModel: FavouriteViewModel){

    val coroutineScope = rememberCoroutineScope()

    // Wyświetlanie listy zdarzeń
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
    ) {

        items(eventList.value.data?.deaths ?: emptyList()) { event ->
            var isMenuExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {

                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = event.year.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopEnd)
                        ){
                            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More"
                                )
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Add to Favourites") },
                                    onClick = {
                                        val favEvent = Favourite(
                                            event.text,
                                            event.pages[0].titles?.normalized.toString(),
                                            event.pages[0].originalimage?.source.toString(),
                                            event.year,
                                            0
                                        )
                                        coroutineScope.launch {
                                            favouriteViewModel.addToFavourites(favEvent)
                                        }

                                        isMenuExpanded = false
                                    })
                            }

                        }

                    }
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
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HolidaysList(eventList: StateFlow<Resource<AllEvents>>, favouriteViewModel: FavouriteViewModel){

    val coroutineScope = rememberCoroutineScope()

    // Wyświetlanie listy zdarzeń
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
    ) {

        items(eventList.value.data?.holidays ?: emptyList()) { event ->

            var isMenuExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {

                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier,
                            text = event.pages[0].titles?.normalized.toString(),
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopEnd)
                        ){
                            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More"
                                )
                            }
                            DropdownMenu(
                                expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Add to Favourites") },
                                    onClick = {
                                        val favEvent = Favourite(
                                            event.text,
                                            event.pages[0].titles?.normalized.toString(),
                                            event.pages[0].originalimage?.source.toString(),
                                            null,
                                            0
                                        )
                                        coroutineScope.launch {
                                            favouriteViewModel.addToFavourites(favEvent)
                                        }

                                        isMenuExpanded = false
                                    })
                            }

                        }

                    }
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
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}