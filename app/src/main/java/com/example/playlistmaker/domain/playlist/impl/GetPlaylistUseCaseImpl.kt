package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.db.repository.PlaylistRepository
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.domain.playlist.GetPlaylistUseCase
import kotlinx.coroutines.flow.Flow

class GetPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository
) : GetPlaylistUseCase {
    override fun execute(playlistId: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylistById(playlistId)
    }
}