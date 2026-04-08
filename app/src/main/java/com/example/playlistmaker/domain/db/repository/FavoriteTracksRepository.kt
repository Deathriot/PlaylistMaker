package com.example.playlistmaker.domain.db.repository

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getAllTracks(): Flow<List<Track>>

    fun getAllTrackId(): Flow<List<Long>>

    fun getTrack(id: Long): Flow<Track?>
}