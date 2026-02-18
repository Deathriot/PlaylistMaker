package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.model.TrackEntity
import com.example.playlistmaker.ui.app.App
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM ${App.FAVORITE_TRACKS_TABLE_NAME} ORDER BY track_created_at DESC")
    fun getAllTracks(): Flow<List<TrackEntity>>

    @Query("SELECT track_id FROM ${App.FAVORITE_TRACKS_TABLE_NAME}")
    fun getAllTrackId(): Flow<List<Long>>

    @Query("SELECT * FROM ${App.FAVORITE_TRACKS_TABLE_NAME} WHERE track_id = :id")
    fun getTrackById(id: Long): Flow<TrackEntity?>
}