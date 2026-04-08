package com.example.playlistmaker.ui.new_playlist.mapper

import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.ui.new_playlist.model.PlaylistInfo

object PlaylistInfoMapper {
    fun mapToInfo(playlist: Playlist): PlaylistInfo {
        return PlaylistInfo(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri
        )
    }
}