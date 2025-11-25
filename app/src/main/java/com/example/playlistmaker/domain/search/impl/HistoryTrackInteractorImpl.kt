package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.repository.HistoryTrackRepository
import com.example.playlistmaker.domain.search.HistoryTrackInteractor

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