package com.example.playlistmaker.domain.db.repository

import com.example.playlistmaker.domain.new_playlist.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)
    suspend fun deletePlaylist(playlistId: Long)
    fun addTrackToPlaylist(trackId: Long, playlistId: Long): Flow<Boolean>
    fun getAllPlayLists(): Flow<List<Playlist>>
    fun getPlaylistById(playlistId: Long): Flow<Playlist?>
}