package com.example.playlistmaker.domain.model

data class Track (
    val id: Long,

    val title: String,

    val artistName: String,

    val timeMillis: Long,

    val artworkUrl100: String,

    val collectionName: String?,

    val releaseDate: String?,

    val primaryGenreName: String,

    val country: String,

    val musicUrl : String
)