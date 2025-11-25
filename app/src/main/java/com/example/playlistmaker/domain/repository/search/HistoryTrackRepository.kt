package com.example.playlistmaker.domain.repository.search

import com.example.playlistmaker.domain.model.Track

interface HistoryTrackRepository {
    fun getAll() : List<Track>

    fun getById(id : Long) : Track?

    fun add(track : Track)

    fun clear()
}