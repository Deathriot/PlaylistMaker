package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.db.repository.PlaylistRepository
import com.example.playlistmaker.domain.db.repository.PlaylistTrackRepository
import com.example.playlistmaker.domain.playlist.DeletePlaylistTrackUseCase

class DeletePlaylistTrackUseCaseImpl(
    private val playlistTrackRepository: PlaylistTrackRepository,
    private val playlistRepository: PlaylistRepository
) : DeletePlaylistTrackUseCase {
    override suspend fun execute(trackId: Long, playlistId: Long) {
        playlistRepository.deleteTrackFromPlaylist(trackId,playlistId)
        playlistTrackRepository.deleteTrackById(trackId)
    }
}