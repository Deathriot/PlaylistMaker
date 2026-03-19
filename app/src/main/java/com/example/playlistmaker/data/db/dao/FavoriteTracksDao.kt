package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.model.FavoriteTrackEntity
import com.example.playlistmaker.ui.app.App
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao {

    @Insert(entity = FavoriteTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: FavoriteTrackEntity)

    @Delete(entity = FavoriteTrackEntity::class)
    suspend fun deleteTrack(trackEntity: FavoriteTrackEntity)

    @Query("SELECT * FROM ${App.FAVORITE_TRACKS_TABLE_NAME} ORDER BY track_created_at DESC")
    fun getAllTracks(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT track_id FROM ${App.FAVORITE_TRACKS_TABLE_NAME}")
    fun getAllTrackId(): Flow<List<Long>>

    @Query("SELECT * FROM ${App.FAVORITE_TRACKS_TABLE_NAME} WHERE track_id = :id")
    fun getTrackById(id: Long): Flow<FavoriteTrackEntity?>
}