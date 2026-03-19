package com.example.playlistmaker.domain.new_playlist

import com.example.playlistmaker.domain.new_playlist.model.Playlist


interface SavePlaylistUseCase {
    suspend fun execute(playList : Playlist)
}