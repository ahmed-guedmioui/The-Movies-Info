package com.ahmedapps.themovies.util.ui_shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.search.presentation.SearchScreenState
import com.ahmedapps.themovies.theme.BigRadius
import com.ahmedapps.themovies.util.Route

@Composable
fun NonFocusedTopBar(
    toolbarOffsetHeightPx: Int,
    navController: NavController,
) {

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(BigRadius.dp)
            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx) }
    ) {
        NonFocusedSearchBar(
            modifier = Modifier
                .height(50.dp)
                .clickable {
                    navController.navigate(Route.SEARCH_SCREEN)
                }
                .padding(horizontal = 8.dp),
            placeholderText = stringResource(R.string.search_for_a_movie_or_tv_series),
        )
    }
}

@Composable
fun FocusedTopBar(
    toolbarOffsetHeightPx: Int,
    searchScreenState: SearchScreenState,
    onSearch: (String) -> Unit = {}
) {

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(BigRadius.dp)
            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx) }
    ) {
        SearchBar(
            leadingIcon = {
                Icon(
                    Icons.Rounded.Search,
                    null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(50.dp),
            placeholderText = stringResource(R.string.search_for_a_movie_or_tv_series),
            searchScreenState = searchScreenState
        ) {
            onSearch(it)
        }
    }
}
