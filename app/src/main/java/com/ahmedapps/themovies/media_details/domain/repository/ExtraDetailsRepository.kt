package com.ahmedapps.themovies.media_details.domain.repository

import com.ahmedapps.themovies.media_details.domain.models.Cast
import com.ahmedapps.themovies.main.domain.models.Media
import com.ahmedapps.themovies.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExtraDetailsRepository {

    suspend fun getSimilarMediaList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>>

    suspend fun getCastList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Cast>>

    suspend fun getVideosList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<List<String>>>

}










