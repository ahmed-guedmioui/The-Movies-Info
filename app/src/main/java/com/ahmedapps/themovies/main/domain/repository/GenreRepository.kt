package com.ahmedapps.themovies.main.domain.repository

import com.ahmedapps.themovies.util.Resource
import com.ahmedapps.themovies.main.domain.models.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String,
        apiKey: String
    ): Flow<Resource<List<Genre>>>
}










