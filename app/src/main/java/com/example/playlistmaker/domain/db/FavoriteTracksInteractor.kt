package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    suspend fun getAllTracks(): Flow<List<Track>>

    suspend fun getAllTrackId(): Flow<List<Long>>

    suspend fun getTrack(id : Long) : Flow<Track?>
}