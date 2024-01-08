package com.ahmedapps.themovies.main.data.remote.api

import com.ahmedapps.themovies.BuildConfig
import com.ahmedapps.themovies.main.data.remote.dto.MediaListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApi {

    @GET("{type}/{category}")
    suspend fun getMoviesAndTvSeriesList(
        @Path("type") type: String,
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MediaListDto

    @GET("trending/{type}/{time}")
    suspend fun getTrendingList(
        @Path("type") type: String,
        @Path("time") time: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MediaListDto

    @GET("search/multi")
    suspend fun getSearchList(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MediaListDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = BuildConfig.api_key

    }

}