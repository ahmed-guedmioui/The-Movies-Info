package com.ahmedapps.themovies.main.data.local.media

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaList(
        mediaEntities: List<MediaEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItem(
        mediaItem: MediaEntity
    )

    @Update
    suspend fun updateMediaItem(
        mediaItem: MediaEntity
    )

    @Query(
        """
            DELETE FROM mediaEntity 
            WHERE mediaType = :mediaType AND category = :category
        """
    )
    suspend fun deleteMediaByTypeAndCategory(mediaType: String, category: String)

    @Query("SELECT * FROM mediaEntity WHERE id = :id")
    suspend fun getMediaById(id: Int): MediaEntity

    @Query(
        """
            SELECT * 
            FROM mediaEntity 
            WHERE mediaType = :mediaType AND category = :category
        """
    )
    suspend fun getMediaListByTypeAndCategory(
        mediaType: String, category: String
    ): List<MediaEntity>

    @Query(
        """
            DELETE FROM mediaEntity 
            WHERE category = :category
        """
    )
    suspend fun deleteTrendingMediaList(category: String)


    @Query(
        """
            SELECT * 
            FROM mediaEntity 
            WHERE category = :category
        """
    )
    suspend fun getTrendingMediaList(category: String): List<MediaEntity>


}