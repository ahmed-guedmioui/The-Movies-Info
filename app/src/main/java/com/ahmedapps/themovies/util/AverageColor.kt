package com.ahmedapps.themovies.util

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

@Composable
fun getAverageColor(imageBitmap: ImageBitmap): Color {
    var averageColor by remember { mutableStateOf(Color.Transparent) }

    LaunchedEffect(Unit) {

        val compatibleBitmap = imageBitmap.asAndroidBitmap()
            .copy(Bitmap.Config.ARGB_8888, false)

        // Retrieve the pixels from the compatible Bitmap
        val pixels = IntArray(compatibleBitmap.width * compatibleBitmap.height)
        compatibleBitmap.getPixels(
            pixels, 0, compatibleBitmap.width, 0, 0,
            compatibleBitmap.width, compatibleBitmap.height
        )

        var redSum = 0
        var greenSum = 0
        var blueSum = 0

        // Calculate the sum of RGB values
        for (pixel in pixels) {
            val red = android.graphics.Color.red(pixel)
            val green = android.graphics.Color.green(pixel)
            val blue = android.graphics.Color.blue(pixel)

            redSum += red
            greenSum += green
            blueSum += blue
        }

        // Calculate the average RGB values
        val pixelCount = pixels.size
        val averageRed = redSum / pixelCount
        val averageGreen = greenSum / pixelCount
        val averageBlue = blueSum / pixelCount

        // Set the average color as the result
        averageColor = Color(averageRed, averageGreen, averageBlue)
    }

    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(averageColor.toArgb(), hsl)

    // Decrease the lightness component by a desired amount
    val darkerLightness = hsl[2] - 0.1f // Adjust the amount to make it darker

    // Create a new color with the modified lightness component
    val darkerColor = ColorUtils.HSLToColor(
        floatArrayOf(
            hsl[0],
            hsl[1], darkerLightness
        )
    )

    return Color(darkerColor)
}
