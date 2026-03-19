package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.player.AddTrackToPlaylistUseCase
import kotlinx.coroutines.flow.Flow

class AddTrackToPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository
) : AddTrackToPlaylistUseCase {
    override fun execute(playlistId: Long, trackId: Long): Flow<Boolean> {
        return playlistRepository.addTrackToPlaylist(trackId, playlistId)
    }
}