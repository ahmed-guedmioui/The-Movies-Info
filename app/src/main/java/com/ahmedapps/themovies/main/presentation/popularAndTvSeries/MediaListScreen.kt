package com.ahmedapps.themovies.main.presentation.popularAndTvSeries

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.main.presentation.main.MainUiEvents
import com.ahmedapps.themovies.main.presentation.main.MainUiState
import com.ahmedapps.themovies.util.ui_shared_components.ListShimmerEffect
import com.ahmedapps.themovies.util.ui_shared_components.MediaItem
import com.ahmedapps.themovies.util.ui_shared_components.NonFocusedTopBar
import com.ahmedapps.themovies.util.ui_shared_components.header
import com.ahmedapps.themovies.theme.BigRadius
import com.ahmedapps.themovies.ui.theme.font
import com.ahmedapps.themovies.util.BottomNavRoute
import com.ahmedapps.themovies.util.Constants.popularScreen
import com.ahmedapps.themovies.util.Constants.recommendedListScreen
import com.ahmedapps.themovies.util.Constants.topRatedAllListScreen
import com.ahmedapps.themovies.util.Constants.trendingAllListScreen
import com.ahmedapps.themovies.util.Constants.tvSeriesScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaListScreen(
    selectedItem: MutableState<Int>,
    navController: NavController,
    bottomBarNavController: NavHostController,
    navBackStackEntry: NavBackStackEntry,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
) {

    val toolbarHeightPx = with(LocalDensity.current) { BigRadius.dp.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    BackHandler(
        enabled = true
    ) {
        selectedItem.value = 0
        bottomBarNavController.navigate(BottomNavRoute.MEDIA_HOME_SCREEN)
    }

    val type = remember {
        navBackStackEntry.arguments?.getString("type")
    }

    val mediaList = when (type) {
        trendingAllListScreen -> mainUiState.trendingAllList
        topRatedAllListScreen -> mainUiState.topRatedAllList
        recommendedListScreen -> mainUiState.recommendedAllList
        tvSeriesScreen -> mainUiState.tvSeriesList
        else -> mainUiState.popularMoviesList
    }


    val title = when (type) {
        trendingAllListScreen -> stringResource(id = R.string.trending)
        topRatedAllListScreen -> stringResource(id = R.string.top_rated)
        tvSeriesScreen -> stringResource(id = R.string.tv_series)
        recommendedListScreen -> stringResource(id = R.string.recommended)
        popularScreen -> stringResource(id = R.string.popular)
        else -> ""
    }


    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }


    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)

        if (type != null) {
            onEvent(MainUiEvents.Refresh(type = type))
        }
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(nestedScrollConnection)
            .pullRefresh(refreshState)
    ) {

        if (mediaList.isEmpty()) {
            ListShimmerEffect(
                title = title,
                BigRadius
            )
        } else {

            val listState = rememberLazyGridState()

            LazyVerticalGrid(
                state = listState,
                contentPadding = PaddingValues(top = BigRadius.dp),
                columns = GridCells.Adaptive(190.dp),
            ) {

                header {
                    Text(
                        modifier = Modifier
                            .padding(
                                vertical = 16.dp,
                                horizontal = 32.dp
                            ),
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = font,
                        fontSize = 20.sp
                    )
                }

                items(mediaList.size) { i ->

                    MediaItem(
                        media = mediaList[i],
                        navController = navController,
                        mainUiState = mainUiState,
                        onEvent = onEvent
                    )

                    if (i >= mediaList.size - 1 && !mainUiState.isLoading) {
                        if (type != null) {
                            onEvent(MainUiEvents.OnPaginate(type = type))
                        }
                    }


                }

            }
        }


        PullRefreshIndicator(
            refreshing,
            refreshState,
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = (BigRadius - 8).dp)
        )

        NonFocusedTopBar(
            toolbarOffsetHeightPx = toolbarOffsetHeightPx.floatValue.roundToInt(),
            navController = navController,
        )

    }
}
