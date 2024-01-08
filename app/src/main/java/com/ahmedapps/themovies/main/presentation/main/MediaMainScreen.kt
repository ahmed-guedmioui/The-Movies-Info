package com.ahmedapps.themovies.main.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmedapps.themovies.main.presentation.home.MediaHomeScreen
import com.ahmedapps.themovies.main.presentation.popularAndTvSeries.MediaListScreen
import com.ahmedapps.themovies.ui.theme.font
import com.ahmedapps.themovies.util.BottomNavRoute
import com.ahmedapps.themovies.util.Constants

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun MediaMainScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
) {

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Popular",
            selectedIcon = Icons.Filled.LocalFireDepartment,
            unselectedIcon = Icons.Outlined.LocalFireDepartment,
        ),
        BottomNavigationItem(
            title = "Tv Series",
            selectedIcon = Icons.Filled.LiveTv,
            unselectedIcon = Icons.Outlined.LiveTv
        )
    )

    val selectedItem = rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomBarNavController = rememberNavController()

    Scaffold(
        content = { paddingValues ->
            BottomNavigationScreens(
                selectedItem = selectedItem,
                modifier = Modifier
                    .padding(
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        },

        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem.intValue == index,
                        onClick = {
                            selectedItem.intValue = index

                            when (selectedItem.intValue) {
                                0 -> {
                                    bottomBarNavController.navigate(BottomNavRoute.MEDIA_HOME_SCREEN)
                                }

                                1 -> bottomBarNavController.navigate(
                                    "${BottomNavRoute.MEDIA_LIST_SCREEN}?type=${Constants.popularScreen}"
                                )

                                2 -> bottomBarNavController.navigate(
                                    "${BottomNavRoute.MEDIA_LIST_SCREEN}?type=${Constants.tvSeriesScreen}"
                                )
                            }

                        },

                        label = {
                            Text(
                                text = item.title,
                                fontFamily = font,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItem.intValue) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }

        }
    )

}

@Composable
fun BottomNavigationScreens(
    selectedItem: MutableState<Int>,
    modifier: Modifier = Modifier,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
) {

    NavHost(
        modifier = modifier,
        navController = bottomBarNavController,
        startDestination = BottomNavRoute.MEDIA_HOME_SCREEN
    ) {

        composable(BottomNavRoute.MEDIA_HOME_SCREEN) {
            MediaHomeScreen(
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

        composable(
            "${BottomNavRoute.MEDIA_LIST_SCREEN}?type={type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            MediaListScreen(
                selectedItem = selectedItem,
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                navBackStackEntry = navBackStackEntry,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

    }
}









