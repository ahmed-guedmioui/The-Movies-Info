package com.ahmedapps.themovies.util.ui_shared_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.main.domain.models.Media
import com.ahmedapps.themovies.main.presentation.main.MainUiEvents

@Composable
fun LikeAndDislike(
    media: Media,
    onEvent: (MainUiEvents) -> Unit
) {

    var isLiked by remember {
        mutableStateOf(false)
    }

    if (isLiked) {
        Box {
            Icon(
                modifier = Modifier
                    .size(27.dp)
                    .clickable {
                        isLiked = !isLiked
                    },
                imageVector = Icons.Rounded.Favorite,
                contentDescription = stringResource(R.string.dislike),
                tint = Color.Red
            )
            Icon(
                modifier = Modifier.size(27.dp),
                imageVector = Icons.Rounded.FavoriteBorder,
                contentDescription = null,
                tint = Color.White
            )
        }
    } else {
        Icon(
            modifier = Modifier
                .size(27.dp)
                .clickable {
                    isLiked = !isLiked
                },
            imageVector = Icons.Rounded.FavoriteBorder,
            contentDescription = stringResource(R.string.like),
            tint = Color.White
        )

    }
}












