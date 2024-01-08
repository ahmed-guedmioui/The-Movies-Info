package com.ahmedapps.themovies.media_details.domain.usecase

/**
 * @author Ahmed Guedmioui
 */
class MinutesToReadableTime(
    private val minutes: Int
) {
    operator fun invoke(): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return String.format("%02d hr %02d min", hours, remainingMinutes)
    }
}