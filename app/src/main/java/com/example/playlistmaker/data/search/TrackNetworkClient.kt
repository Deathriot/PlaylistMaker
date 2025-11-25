package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.model.TrackNetworkResponse
import com.example.playlistmaker.domain.consumer.Consumer

interface TrackNetworkClient {
    fun getTracks(title: String, consumer: Consumer<TrackNetworkResponse?>)
}