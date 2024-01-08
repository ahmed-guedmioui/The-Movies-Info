package com.ahmedapps.themovies.util.ui_shared_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahmedapps.themovies.main.presentation.main.MainUiState

import com.ahmedapps.themovies.ui.theme.font

@Composable
fun AutoSwipeSection(
    type: String,
    navController: NavController,
    mainUiState: MainUiState,
) {

    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = font,
                fontSize = 20.sp
            )
        }

        AutoSwipeImagePager(
            mediaList = mainUiState.specialList.take(7),
            navController = navController,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(0.9f)
        )
    }

}
