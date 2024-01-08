package com.ahmedapps.themovies.main.data.local.media

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MediaEntity(
    @PrimaryKey val id: Int,

    val adult: Boolean,
    val backdropPath: String,
    val firstAirDate: String,
    val genreIds: String,
    var mediaType: String,
    val originCountry: String,
    val originalLanguage: String,
    val originalName: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    var category: String,
    var runtime: Int,
    var videos: String,
    var status: String,
    var tagline: String,
    var similarMediaList: String,
)
