package com.mateusz.onthisday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mateusz.onthisday.data.db.FavouriteViewModel
import com.mateusz.onthisday.eventlist.EventListScreen
import com.mateusz.onthisday.eventlist.EventListViewModel
import com.mateusz.onthisday.ui.screen.FavouriteScreen
import com.mateusz.onthisday.ui.screen.SettingsScreen
import com.mateusz.onthisday.ui.theme.OnThisDayTheme
import dagger.hilt.android.AndroidEntryPoint


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)
@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by remember { mutableStateOf(true) }
            OnThisDayTheme(darkTheme) {
                val navController = rememberNavController()
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        hasNews = false,
                    ),
                    BottomNavigationItem(
                        title = "Favourites",
                        selectedIcon = Icons.Filled.Star,
                        unselectedIcon = Icons.Outlined.Star,
                        hasNews = false,
                    ),
                    BottomNavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                        hasNews = false,
                    ),
                )
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            navController.navigate(item.title)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        alwaysShowLabel = false,
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if(item.badgeCount != null) {
                                                        Badge {
                                                            Text(text = item.badgeCount.toString())
                                                        }
                                                    } else if(item.hasNews) {
                                                        Badge()
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (index == selectedItemIndex) {
                                                        item.selectedIcon
                                                    } else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues),
                            verticalArrangement = Arrangement.spacedBy(16.dp)

                        ){
                            val viewModel: EventListViewModel = viewModel()
                            val favouriteViewModel: FavouriteViewModel = viewModel()
                            NavHost(navController = navController, startDestination = "Home"){
                                composable("Home"){
                                    EventListScreen(viewModel, favouriteViewModel)
                                }
                                composable("Favourites"){
                                    FavouriteScreen(favouriteViewModel = favouriteViewModel)
                                }
                                composable("Settings"){
                                    SettingsScreen(
                                        darkTheme = darkTheme,
                                        onThemeUpdated = { darkTheme = !darkTheme}
                                        )
                                }
                            }

                        }



                    }
                }
            }
        } //theme end
    }
}


