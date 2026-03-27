package com.example.playlistmaker.domain.playlist.navigation

import com.example.playlistmaker.domain.playlist.navigation.model.PlaylistForShare
import com.example.playlistmaker.domain.playlist.navigation.model.TrackForShare

interface SharePlaylistUseCase {
    fun execute(playlist: PlaylistForShare, tracks: List<TrackForShare>)
}