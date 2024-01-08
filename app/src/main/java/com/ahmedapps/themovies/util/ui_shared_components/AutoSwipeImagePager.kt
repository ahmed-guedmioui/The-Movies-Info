package com.ahmedapps.themovies.util.ui_shared_components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahmedapps.themovies.main.domain.models.Media
import com.ahmedapps.themovies.theme.Radius


import com.ahmedapps.themovies.ui.theme.font
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSwipeImagePager(
    mediaList: List<Media>,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        mediaList.size
    }

    val scope = rememberCoroutineScope()
    val swipeIntervalMillis = 3000

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth(),
            state = pagerState,
            key = {
                mediaList[it].id
            },
            pageSize = PageSize.Fill
        ) { index ->

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = modifier
                        .clip(RoundedCornerShape(Radius.dp))
                ) {
                    Item(
                        media = mediaList[index],
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black
                                    )
                                )
                            )
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 12.dp,
                                top = 22.dp,
                            )
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = mediaList[index].title,
                            color = Color.White,
                            fontFamily = font,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                        )
                    }
                }
            }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(swipeIntervalMillis.toLong())
                    scope.launch {

                        if (pagerState.canScrollForward) {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )
                        } else {
                            pagerState.animateScrollToPage(
                                0
                            )
                        }

                    }
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            DotsIndicator(
                totalDots = mediaList.size,
                selectedIndex = pagerState.currentPage
            )

        }
    }
}
