package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.model.TrackNetworkResponse
import com.example.playlistmaker.domain.consumer.Consumer

interface TrackNetworkClient {
    fun getTracks(title: String, consumer: Consumer<TrackNetworkResponse?>)
}