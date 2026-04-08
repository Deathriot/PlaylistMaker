package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.model.PlaylistTrackEntity
import com.example.playlistmaker.ui.app.App
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTracksDao {
    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrackEntity: PlaylistTrackEntity)

    @Query("DELETE FROM ${App.PLAYLIST_TRACKS_TABLE_NAME} WHERE track_id = :trackId")
    suspend fun deleteTrack(trackId: Long)

    @Query("DELETE FROM ${App.PLAYLIST_TRACKS_TABLE_NAME} WHERE track_id IN (:trackIds)")
    suspend fun deleteTracks(trackIds: List<Long>)

    @Query("SELECT * FROM ${App.PLAYLIST_TRACKS_TABLE_NAME} WHERE track_id IN (:trackIds)")
    fun getTracksByIds(trackIds: List<Long>): Flow<List<PlaylistTrackEntity>>

    @Query("SELECT * FROM ${App.PLAYLIST_TRACKS_TABLE_NAME} WHERE track_id =:trackId")
    fun getTrackById(trackId: Long) : Flow<PlaylistTrackEntity?>
}