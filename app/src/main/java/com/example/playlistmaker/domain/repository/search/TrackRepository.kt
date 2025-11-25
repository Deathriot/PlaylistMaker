package com.example.playlistmaker.domain.repository.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.model.Track

interface TrackRepository {
    fun getTracks(title : String, consumer : Consumer<List<Track>?>)

    fun getTrackById(id: Long) : Track?
}