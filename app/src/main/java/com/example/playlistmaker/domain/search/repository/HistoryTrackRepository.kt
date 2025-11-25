package com.example.playlistmaker.domain.search.repository

import com.example.playlistmaker.domain.search.model.Track

interface HistoryTrackRepository {
    fun getAll() : List<Track>

    fun getById(id : Long) : Track?

    fun add(track : Track)

    fun clear()
}