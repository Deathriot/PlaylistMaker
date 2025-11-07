package com.example.playlistmaker.data.model

abstract class TrackNetworkResponse {
    abstract val tracks : List<TrackNetworkDto>
    var resultCode = 0
}
