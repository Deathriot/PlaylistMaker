package com.example.playlistmaker.ui.playlist.model

import android.net.Uri

data class PlaylistDetailsInfo(
    val id: Long = 0,

    val name: String,

    val description: String?,

    val coverUri: Uri?,

    val tracksIds: String = "[]",

    var totalDuration: Int,

    val trackCount: Int = 0
)