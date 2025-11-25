package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface HistoryTrackInteractor {
    fun getAllTracks() : List<Track>

    fun getTrackById(id : Long) : Track?

    fun addTrack(track : Track)

    fun clearTracks()
}