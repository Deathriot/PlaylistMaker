package com.example.playlistmaker.domain.use_case.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.search.TrackRepository

class GetTracksUseCaseImpl(
    private val repository: TrackRepository
) : GetTracksUseCase {
    override fun execute(title: String, consumer: Consumer<List<Track>?>) {
        repository.getTracks(
            title = title,
            consumer = consumer
        )
    }

    override fun getById(id: Long): Track? {
        return repository.getTrackById(id)
    }
}