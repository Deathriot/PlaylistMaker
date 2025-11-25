package com.example.playlistmaker.data.search.model

abstract class TrackNetworkResponse {
    abstract val tracks : List<TrackNetworkDto>
    var resultCode = 0
}
