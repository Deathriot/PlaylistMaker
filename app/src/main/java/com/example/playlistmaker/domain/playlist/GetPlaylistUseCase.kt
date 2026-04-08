package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.new_playlist.model.Playlist
import kotlinx.coroutines.flow.Flow

interface GetPlaylistUseCase {
    fun execute(playlistId: Long): Flow<Playlist?>
}