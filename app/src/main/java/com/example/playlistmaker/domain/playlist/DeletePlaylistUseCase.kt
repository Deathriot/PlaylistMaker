package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.new_playlist.model.Playlist

interface DeletePlaylistUseCase {
    suspend fun execute(playlist: Playlist)
}