package com.example.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface AddTrackToPlaylistUseCase {
    fun execute(playlistId: Long, trackId: Long): Flow<Boolean>
}