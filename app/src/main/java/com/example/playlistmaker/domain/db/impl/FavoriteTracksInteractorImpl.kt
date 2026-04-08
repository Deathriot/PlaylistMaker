package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.repository.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl (
    private val repository: FavoriteTracksRepository
): FavoriteTracksInteractor {
    override suspend fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return repository.getAllTracks()
    }

    override suspend fun getAllTrackId(): Flow<List<Long>> {
        return repository.getAllTrackId()
    }

    override suspend fun getTrack(id: Long): Flow<Track?> {
        return repository.getTrack(id)
    }
}