package com.example.playlistmaker.data.model


abstract class  TrackNetworkDto {
    abstract val id: Long

    abstract val title: String

    abstract val artistName: String

    abstract val artworkUrl100: String

    abstract val timeMillis: Long

    abstract val collectionName: String?

    abstract val releaseDate: String?

    abstract val primaryGenreName: String

    abstract val country: String

    abstract val musicUrl: String
}