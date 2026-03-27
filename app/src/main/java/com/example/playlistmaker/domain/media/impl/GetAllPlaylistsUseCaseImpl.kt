package com.example.playlistmaker.domain.media.impl

import com.example.playlistmaker.domain.db.repository.PlaylistRepository
import com.example.playlistmaker.domain.media.GetAllPlaylistsUseCase
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import kotlinx.coroutines.flow.Flow

class GetAllPlaylistsUseCaseImpl(
    private val playlistRepository: PlaylistRepository,

    ) : GetAllPlaylistsUseCase {
    override fun execute(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlayLists()
    }
}