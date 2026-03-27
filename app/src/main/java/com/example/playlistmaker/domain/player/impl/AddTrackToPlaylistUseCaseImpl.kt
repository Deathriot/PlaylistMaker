package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.db.repository.PlaylistRepository
import com.example.playlistmaker.domain.db.repository.PlaylistTrackRepository
import com.example.playlistmaker.domain.player.AddTrackToPlaylistUseCase
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class AddTrackToPlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistTrackRepository: PlaylistTrackRepository
) : AddTrackToPlaylistUseCase {
    override suspend fun execute(playlistId: Long, track: Track): Flow<Boolean> {
        playlistTrackRepository.insertTrack(track)
        return playlistRepository.addTrackToPlaylist(track.id, playlistId)
    }
}