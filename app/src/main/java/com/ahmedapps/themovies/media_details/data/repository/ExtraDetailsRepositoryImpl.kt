package com.ahmedapps.themovies.media_details.data.repository

import com.ahmedapps.themovies.media_details.data.remote.api.ExtraDetailsApi
import com.ahmedapps.themovies.media_details.domain.models.Cast
import com.ahmedapps.themovies.media_details.domain.repository.ExtraDetailsRepository
import com.ahmedapps.themovies.main.data.local.media.MediaDatabase
import com.ahmedapps.themovies.main.data.local.media.MediaEntity
import com.ahmedapps.themovies.main.data.mappers.toMedia
import com.ahmedapps.themovies.main.data.mappers.toMediaEntity
import com.ahmedapps.themovies.main.data.remote.dto.MediaDto
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
class ExtraDetailsRepositoryImpl @Inject constructor(
    private val extraDetailsApi: ExtraDetailsApi,
    mediaDb: MediaDatabase
) : ExtraDetailsRepository {

    private val mediaDao = mediaDb.mediaDao

    override suspend fun getSimilarMediaList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {

        return flow {

            emit(Resource.Loading(true))

            val mediaEntity = mediaDao.getMediaById(id = id)

            val doesSimilarMediaListExist =
                (mediaEntity.similarMediaList != null && mediaEntity.similarMediaList != "-1,-2")

            if (!isRefresh && doesSimilarMediaListExist) {

                try {
                    val similarMediaListIds =
                        mediaEntity.similarMediaList?.split(",")!!.map { it.toInt() }

                    val similarMediaEntityList = ArrayList<MediaEntity>()
                    for (i in similarMediaListIds.indices) {
                        similarMediaEntityList.add(mediaDao.getMediaById(similarMediaListIds[i]))
                    }
                    emit(
                        Resource.Success(
                            data = similarMediaEntityList.map {
                                it.toMedia(
                                    type = it.mediaType ?: Constants.MOVIE,
                                    category = it.category ?: Constants.POPULAR
                                )
                            }
                        )
                    )
                } catch (e: Exception) {
                    emit(Resource.Error("Something went wrong."))
                }


                emit(Resource.Loading(false))
                return@flow


            }

            val remoteSimilarMediaList = fetchRemoteForSimilarMediaList(
                type = mediaEntity.mediaType ?: Constants.MOVIE,
                id = id,
                page = page,
                apiKey = apiKey
            )

            if (remoteSimilarMediaList == null) {
                emit(
                    Resource.Success(
                        data = emptyList()
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            remoteSimilarMediaList.let { similarMediaList ->

                val similarMediaListIntIds = ArrayList<Int>()
                for (i in similarMediaList.indices) {
                    similarMediaListIntIds.add(similarMediaList[i].id ?: -1)
                }

                mediaEntity.similarMediaList = try {
                    similarMediaListIntIds.joinToString(",")
                } catch (e: Exception) {
                    "-1,-2"
                }

                val similarMediaEntityList = remoteSimilarMediaList.map {
                    it.toMediaEntity(
                        type = it.media_type ?: Constants.MOVIE,
                        category = mediaEntity.category ?: Constants.POPULAR
                    )
                }

                mediaDao.insertMediaList(similarMediaEntityList)
                mediaDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(
                        data = similarMediaEntityList.map {
                            it.toMedia(
                                type = it.mediaType ?: Constants.MOVIE,
                                category = it.category ?: Constants.POPULAR
                            )
                        }
                    )
                )

                emit(Resource.Loading(false))

            }


        }

    }

    private suspend fun fetchRemoteForSimilarMediaList(
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): List<MediaDto>? {

        val remoteSimilarMediaList = try {
            extraDetailsApi.getSimilarMediaList(
                type = type,
                id = id,
                page = page,
                apiKey = apiKey
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        return remoteSimilarMediaList?.results

    }


    override suspend fun getVideosList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<List<String>>> {

        return flow {

            emit(Resource.Loading(true))

            val mediaEntity = mediaDao.getMediaById(id = id)

            val doVideosExist = (mediaEntity.videos != null)

            if (!isRefresh && doVideosExist) {

                try {
                    val videosIds =
                        mediaEntity.videos?.split(",")!!.map { it }

                    emit(
                        Resource.Success(data = videosIds)
                    )
                } catch (e: Exception) {
                    emit(Resource.Error("Something went wrong."))
                }

                emit(Resource.Loading(false))
                return@flow


            }

            val videosIds = fetchRemoteForVideosIds(
                type = mediaEntity.mediaType ?: Constants.MOVIE,
                id = id,
                apiKey = apiKey
            )

            if (videosIds == null) {
                emit(
                    Resource.Success(
                        data = emptyList()
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            videosIds.let {
                mediaEntity.videos = try {
                    it.joinToString(",")
                } catch (e: Exception) {
                    "-1,-2"
                }

                mediaDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(data = videosIds)
                )

                emit(Resource.Loading(false))

            }


        }
    }


    private suspend fun fetchRemoteForVideosIds(
        type: String,
        id: Int,
        apiKey: String
    ): List<String>? {

        val remoteVideosIds = try {
            extraDetailsApi.getVideosList(
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

        val listToReturn = remoteVideosIds?.results?.filter {
            it.site == "YouTube" && it.type == "Featurette" || it.type == "Teaser"
        }

        return listToReturn?.map {
            it.key
        }

    }



    // later
    override suspend fun getCastList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Cast>> {

        return flow {

//            emit(Resource.Loading(true))
//
//            val mediaEntity = mediaDao.getMediaById(id = id)
//
//            val doCastExist = !(mediaEntity.runtime == null ||
//                    mediaEntity.status == null || mediaEntity.tagline == null)
//
//            if (!isRefresh && doCastExist) {
//                emit(
//                    Resource.Success(
//                        data = mediaEntity.toMedia(
//                            type = mediaEntity.mediaType ?: Constants.MOVIE,
//                            category = mediaEntity.category ?: Constants.POPULAR
//                        )
//                    )
//                )
//
//                emit(Resource.Loading(false))
//                return@flow
//            }
//
//            val remoteDetails = fetchRemoteForDetails(
//                type = mediaEntity.mediaType ?: Constants.MOVIE,
//                id = id,
//                apiKey = apiKey
//            )
//
//            if (remoteDetails == null) {emit(
//                Resource.Success(
//                    data = mediaEntity.toMedia(
//                        type = mediaEntity.mediaType ?: Constants.MOVIE,
//                        category = mediaEntity.category ?: Constants.POPULAR
//                    )
//                )
//            )
//                emit(Resource.Loading(false))
//                return@flow
//            }
//
//            remoteDetails.let { details ->
//
//                mediaEntity.runtime = details.runtime
//                mediaEntity.status = details.status
//                mediaEntity.tagline = details.tagline
//
//                mediaDao.updateMediaItem(mediaEntity)
//
//                emit(
//                    Resource.Success(
//                        data = mediaEntity.toMedia(
//                            type = mediaEntity.mediaType ?: Constants.MOVIE,
//                            category = mediaEntity.category ?: Constants.POPULAR
//                        )
//                    )
//                )
//
//                emit(Resource.Loading(false))
//
//            }


        }

    }


}










