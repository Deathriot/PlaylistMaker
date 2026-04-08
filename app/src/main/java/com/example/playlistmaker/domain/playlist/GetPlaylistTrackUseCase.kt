package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface GetPlaylistTrackUseCase {
    fun execute(trackId: Long): Flow<Track?>
}