package com.example.playlistmaker.ui.media.model

data class PlaylistDetails (
    val id: Long,

    val name: String,

    val coverUri: String?,

    val trackCount: Int
)