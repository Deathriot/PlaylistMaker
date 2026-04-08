package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.db.repository.PlaylistTrackRepository
import com.example.playlistmaker.domain.playlist.GetPlaylistTrackUseCase
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class GetPlaylistTrackUseCaseImpl(
    private val playlistTrackRepository: PlaylistTrackRepository
) : GetPlaylistTrackUseCase {
    override fun execute(trackId: Long): Flow<Track?> {
        return playlistTrackRepository.getTrackById(trackId)
    }
}