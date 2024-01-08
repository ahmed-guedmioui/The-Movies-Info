package com.ahmedapps.themovies.media_details.domain.repository

import com.ahmedapps.themovies.main.domain.models.Media
import com.ahmedapps.themovies.util.Resource
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    suspend fun getDetails(
        type: String,
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Media>>

}










