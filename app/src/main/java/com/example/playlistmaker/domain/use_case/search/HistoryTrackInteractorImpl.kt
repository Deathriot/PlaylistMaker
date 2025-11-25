package com.example.playlistmaker.domain.use_case.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.search.HistoryTrackRepository

class HistoryTrackInteractorImpl (
    private val repository : HistoryTrackRepository
): HistoryTrackInteractor {
    override fun getAllTracks(): List<Track> {
        return repository.getAll().reversed()
    }

    override fun getTrackById(id: Long): Track? {
        return repository.getById(id)
    }

    override fun addTrack(track: Track) {
        repository.add(track)
    }

    override fun clearTracks() {
        repository.clear()
    }
}