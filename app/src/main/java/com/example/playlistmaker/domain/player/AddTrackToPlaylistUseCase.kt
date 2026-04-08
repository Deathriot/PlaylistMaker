package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface AddTrackToPlaylistUseCase {
    suspend fun execute(playlistId: Long, track: Track): Flow<Boolean>
}