package com.ahmedapps.themovies.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ahmedapps.themovies.R

val font = FontFamily(
    Font(R.font.font, FontWeight.Normal),
)

val Typography = Typography(
    titleSmall = TextStyle(
        fontFamily = font,
        fontSize = 18.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)