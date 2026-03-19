package com.example.playlistmaker.domain.media

import com.example.playlistmaker.domain.new_playlist.model.Playlist
import kotlinx.coroutines.flow.Flow

interface GetAllPlaylistsUseCase {
    fun execute() : Flow<List<Playlist>>
}