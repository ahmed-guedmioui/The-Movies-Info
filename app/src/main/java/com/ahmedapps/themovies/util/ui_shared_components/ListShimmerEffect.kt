package com.ahmedapps.themovies.util.ui_shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmedapps.themovies.theme.Radius
import com.ahmedapps.themovies.theme.RadiusContainer
import com.ahmedapps.themovies.ui.theme.font

@Composable
fun ListShimmerEffect(
    title: String,
    radius: Int
) {
    LazyVerticalGrid(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(top = radius.dp),
        columns = GridCells.Fixed(2),
    ) {

        header {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 32.dp
                        ),
                    textAlign = TextAlign.Center,
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = font,
                    fontSize = 20.sp
                )
            }
        }

        items(50) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp, start = 8.dp, end = 8.dp
                    )
                    .clip(RoundedCornerShape(RadiusContainer.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth()
                        .padding(6.dp)
                        .clip(RoundedCornerShape(Radius.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(15.dp)
                        .padding(start = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(11.dp)
                        .padding(start = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(12.dp)
                        .padding(start = 11.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}



