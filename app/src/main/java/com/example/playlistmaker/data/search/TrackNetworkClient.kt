package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.model.TrackNetworkResponse

interface TrackNetworkClient {
    suspend fun getTracks(title: String): TrackNetworkResponse
}