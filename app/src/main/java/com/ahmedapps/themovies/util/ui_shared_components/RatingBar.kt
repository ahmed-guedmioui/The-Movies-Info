package com.ahmedapps.themovies.util.ui_shared_components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.ui.theme.yellow

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    starsModifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = yellow,
) {

    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (stars - kotlin.math.ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))

    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                modifier = starsModifier,
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                tint = starsColor
            )
        }
        if (halfStar) {
            Icon(
                modifier = starsModifier,
                painter = painterResource(id = R.drawable.ic_half_star),
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                modifier = starsModifier,
                painter = painterResource(id = R.drawable.ic_empty_star),
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}