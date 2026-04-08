package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.model.PlaylistEntity
import com.example.playlistmaker.ui.app.App
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM ${App.PLAYLIST_TABLE_NAME} WHERE playlist_id =:playlistId")
    suspend fun deletePlaylistById(playlistId: Long)

    @Query("SELECT * FROM ${App.PLAYLIST_TABLE_NAME} ORDER BY playlist_created_at DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM ${App.PLAYLIST_TABLE_NAME} WHERE playlist_id =:id")
    fun getPlaylist(id: Long): Flow<PlaylistEntity?>
}