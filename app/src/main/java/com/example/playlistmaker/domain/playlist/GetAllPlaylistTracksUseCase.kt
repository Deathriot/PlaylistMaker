package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface GetAllPlaylistTracksUseCase {
    fun execute(jsonIds: String) : Flow<List<Track>>
}