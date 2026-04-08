package com.example.playlistmaker.domain.db.repository

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistTrackRepository {
    suspend fun insertTrack(track: Track)
    fun getTracksByIds(jsonIds: String): Flow<List<Track>>
    fun getTrackById(trackId: Long): Flow<Track?>
    suspend fun deleteTrackById(trackId: Long)
    suspend fun deleteAllTracksFromPlaylist(playlistId: Long)
}