package com.ahmedapps.themovies.main.data.remote.dto

data class MediaListDto(
    val page: Int,
    val results: List<MediaDto>,
    val total_pages: Int,
    val total_results: Int
)