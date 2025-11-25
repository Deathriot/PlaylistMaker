package com.example.playlistmaker.domain.use_case.search

import com.example.playlistmaker.domain.model.Track

interface HistoryTrackInteractor {
    fun getAllTracks() : List<Track>

    fun getTrackById(id : Long) : Track?

    fun addTrack(track : Track)

    fun clearTracks()
}