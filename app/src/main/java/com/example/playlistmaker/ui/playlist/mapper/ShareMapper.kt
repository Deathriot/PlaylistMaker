package com.example.playlistmaker.ui.playlist.mapper

import com.example.playlistmaker.domain.playlist.navigation.model.PlaylistForShare
import com.example.playlistmaker.domain.playlist.navigation.model.TrackForShare
import com.example.playlistmaker.ui.playlist.model.PlaylistDetailsInfo
import com.example.playlistmaker.ui.search.model.TrackInfo

object ShareMapper {
    fun mapToTrackForShare(trackInfo: TrackInfo): TrackForShare {
        return TrackForShare(
            title = trackInfo.title,
            artistName = trackInfo.artistName,
            time = trackInfo.time
        )
    }

    fun mapToPlaylistForShare(playlistDetailsInfo: PlaylistDetailsInfo): PlaylistForShare {
        return PlaylistForShare(
            name = playlistDetailsInfo.name,
            description = playlistDetailsInfo.description,
            trackCount = playlistDetailsInfo.trackCount
        )
    }
}