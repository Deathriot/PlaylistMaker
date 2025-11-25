package com.example.playlistmaker.domain.search.repository

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.model.Track

interface TrackRepository {
    fun getTracks(title : String, consumer : Consumer<List<Track>?>)

    fun getTrackById(id: Long) : Track?
}