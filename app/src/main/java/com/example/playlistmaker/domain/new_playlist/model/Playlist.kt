package com.example.playlistmaker.domain.new_playlist.model

import android.net.Uri

data class Playlist(
    val id: Long = 0,

    val name: String,

    val description: String?,

    val coverUri: Uri?,

    val tracksIds: String = "[]",

    val trackCount: Int = 0
)