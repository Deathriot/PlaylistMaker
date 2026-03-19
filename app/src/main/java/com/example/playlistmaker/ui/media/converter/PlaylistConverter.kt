package com.example.playlistmaker.ui.media.converter

import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.ui.media.model.PlaylistDetails

object PlaylistConverter {
    fun convertToDetails(playlist: Playlist): PlaylistDetails {
        return PlaylistDetails(
            id = playlist.id,
            name = playlist.name,
            coverUri = playlist.coverUri.toString(),
            trackCount = playlist.trackCount
        )
    }
}