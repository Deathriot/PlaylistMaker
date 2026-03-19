package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.new_playlist.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)
    fun addTrackToPlaylist(trackId: Long, playlistId: Long): Flow<Boolean>
    fun getAllPlayLists(): Flow<List<Playlist>>
}