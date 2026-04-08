package com.example.playlistmaker.ui.new_playlist.model

import android.net.Uri

data class PlaylistInfo(
    val id: Long,

    val name: String,

    val description: String?,

    val coverUri: Uri?
)