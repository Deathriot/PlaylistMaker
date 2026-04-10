package com.example.playlistmaker.data.db.converter

import androidx.core.net.toUri
import com.example.playlistmaker.data.db.model.PlaylistEntity
import com.example.playlistmaker.domain.new_playlist.model.Playlist

object PlaylistEntityConvertor {
    fun convertToPlaylist(playlistEntity: PlaylistEntity?): Playlist? {
        return if (playlistEntity != null) Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            tracksIds = playlistEntity.tracksIds,
            coverUri = playlistEntity.coverUri?.toUri(),
            trackCount = playlistEntity.trackCount
        ) else null
    }

    fun convertToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksIds = playlist.tracksIds,
            coverUri = playlist.coverUri?.toString(),
            trackCount = playlist.trackCount
        )
    }
}