package com.ahmedapps.themovies.search.domain.repository

import com.ahmedapps.themovies.util.Resource
import com.ahmedapps.themovies.main.domain.models.Media
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getSearchList(
        fetchFromRemote: Boolean,
        query: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>>

}










