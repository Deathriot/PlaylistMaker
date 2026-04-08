package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.db.repository.PlaylistTrackRepository
import com.example.playlistmaker.domain.playlist.GetAllPlaylistTracksUseCase
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class GetAllPlaylistTracksUseCaseImpl(
    private val playlistTrackRepository: PlaylistTrackRepository
) : GetAllPlaylistTracksUseCase {
    override fun execute(jsonIds: String): Flow<List<Track>> {
        return playlistTrackRepository.getTracksByIds(jsonIds)
    }
}