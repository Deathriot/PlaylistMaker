package com.example.playlistmaker.ui.search.model

data class TrackInfo(
    val id: Long,

    val title: String,

    val artistName: String,

    var time: String,

    val artworkUrl100: String,
)