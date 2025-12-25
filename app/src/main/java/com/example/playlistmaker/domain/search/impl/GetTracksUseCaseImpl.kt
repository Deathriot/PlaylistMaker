package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.repository.TrackRepository
import com.example.playlistmaker.domain.search.GetTracksUseCase
import kotlinx.coroutines.flow.Flow

class GetTracksUseCaseImpl(
    private val repository: TrackRepository
) : GetTracksUseCase {

    override fun execute(title: String): Flow<Result<List<Track>?>> {
        return repository.getTracks(title)
    }

    override fun getById(id: Long): Track? {
        return repository.getTrackById(id)
    }
}