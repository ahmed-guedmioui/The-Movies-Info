package com.ahmedapps.themovies.util.ui_shared_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.main.presentation.main.MainUiState
import com.ahmedapps.themovies.main.presentation.home.MediaHomeScreenSection
import com.ahmedapps.themovies.theme.Radius
import com.ahmedapps.themovies.ui.theme.font
import com.ahmedapps.themovies.util.Constants


@Composable
fun ShouldShowMediaHomeScreenSectionOrShimmer(
    type: String,
    showShimmer: Boolean,
    navController: NavController,
    navHostController: NavHostController,
    mainUiState: MainUiState
) {

    val title = when (type) {
        Constants.trendingAllListScreen -> {
            stringResource(id = R.string.trending)
        }

        Constants.tvSeriesScreen -> {
            stringResource(id = R.string.tv_series)
        }

        Constants.recommendedListScreen -> {
            stringResource(id = R.string.recommended)
        }

        else -> {
            stringResource(id = R.string.top_rated)
        }
    }

    if (showShimmer) {
        ShowHomeShimmer(
            title = title,
            modifier = Modifier
                .height(220.dp)
                .width(150.dp)
                .padding(
                    top = 20.dp,
                    start = 16.dp,
                    bottom = 12.dp
                ),
            paddingEnd = 16.dp
        )
    } else {
        MediaHomeScreenSection(
            type = type,
            navController = navController,
            bottomBarNavController = navHostController,
            mainUiState = mainUiState
        )
    }
}

@Composable
fun ShowHomeShimmer(
    title: String,
    paddingEnd: Dp,
    modifier: Modifier = Modifier,
) {

    Column (
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 22.dp,
            ),
            fontWeight = FontWeight.Bold,
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = font,
            fontSize = 20.sp
        )

        LazyRow {
            items(10) {
                Box(
                    modifier = modifier
                        .padding(
                            end = if (it == 9) paddingEnd else 0.dp
                        )
                        .clip(RoundedCornerShape(Radius.dp))
                        .shimmerEffect(false)
                )
            }
        }
    }
}
