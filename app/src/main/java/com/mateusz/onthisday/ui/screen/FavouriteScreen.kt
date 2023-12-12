package com.mateusz.onthisday

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mateusz.onthisday.data.db.FavouriteViewModel
import com.mateusz.onthisday.data.models.Favourite
import kotlinx.coroutines.launch


@Composable
fun FavouriteScreen(favouriteViewModel: FavouriteViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val favourites = favouriteViewModel.getFavorites().collectAsState(initial = emptyList())
    val isHoliday = false

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
    ) {

        items(favourites.value.size) { favourite ->

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
                        if(favourites.value[favourite].year == null){
                            Text(
                                text = favourites.value[favourite].title.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = favourites.value[favourite].year.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

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
                                    text = { Text(text = "Remove from Favourites") },
                                    onClick = {
                                        val favEvent = Favourite(
                                            favourites.value[favourite].text,
                                            favourites.value[favourite].title,
                                            favourites.value[favourite].originalimage,
                                            favourites.value[favourite].year,
                                            favourites.value[favourite].id
                                        )
                                        coroutineScope.launch {
                                            favouriteViewModel.removeFromFavourites(favEvent)
                                        }

                                        isMenuExpanded = false
                                    })
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    if(favourites.value[favourite].year != null){
                        Text(
                            modifier = Modifier,
                            text = favourites.value[favourite].title.toString(),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = favourites.value[favourite].text.toString())
                }


                Spacer(modifier = Modifier.height(8.dp))


                AsyncImage(
                    model = favourites.value[favourite].originalimage,
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
