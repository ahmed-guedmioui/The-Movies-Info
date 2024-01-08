package com.ahmedapps.themovies.media_details.data.repository

import com.ahmedapps.themovies.media_details.data.remote.api.ExtraDetailsApi
import com.ahmedapps.themovies.media_details.data.remote.dto.details.DetailsDto
import com.ahmedapps.themovies.media_details.domain.repository.DetailsRepository
import com.ahmedapps.themovies.main.data.local.media.MediaDatabase
import com.ahmedapps.themovies.main.data.mappers.toMedia
import com.ahmedapps.themovies.main.domain.models.Media
import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepositoryImpl @Inject constructor(
    private val extraDetailsApi: ExtraDetailsApi,
    mediaDb: MediaDatabase
) : DetailsRepository {

    private val mediaDao = mediaDb.mediaDao

    override suspend fun getDetails(
        type: String,
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Media>> {

        return flow {

            emit(Resource.Loading(true))

            val mediaEntity = mediaDao.getMediaById(id = id)

            val doDetailsExist = !(mediaEntity.runtime == null ||
                    mediaEntity.status == null || mediaEntity.tagline == null)

            if (!isRefresh && doDetailsExist) {
                emit(
                    Resource.Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType ?: Constants.MOVIE,
                            category = mediaEntity.category ?: Constants.POPULAR
                        )
                    )
                )

                emit(Resource.Loading(false))
                return@flow
            }

            val remoteDetails = fetchRemoteForDetails(
                type = mediaEntity.mediaType ?: Constants.MOVIE,
                id = id,
                apiKey = apiKey
            )

            if (remoteDetails == null) {emit(
                Resource.Success(
                    data = mediaEntity.toMedia(
                        type = mediaEntity.mediaType ?: Constants.MOVIE,
                        category = mediaEntity.category ?: Constants.POPULAR
                    )
                )
            )
                emit(Resource.Loading(false))
                return@flow
            }

            remoteDetails.let { details ->

                mediaEntity.runtime = details.runtime
                mediaEntity.status = details.status
                mediaEntity.tagline = details.tagline

                mediaDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType ?: Constants.MOVIE,
                            category = mediaEntity.category ?: Constants.POPULAR
                        )
                    )
                )

                emit(Resource.Loading(false))

            }


        }

    }

    private suspend fun fetchRemoteForDetails(
        type: String,
        id: Int,
        apiKey: String
    ): DetailsDto? {
        val remoteDetails = try {
            extraDetailsApi.getDetails(
                type = type,
                id = id,
                apiKey = apiKey
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        return remoteDetails

    }

}










