package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.repository.TrackRepository
import com.example.playlistmaker.domain.search.GetTracksUseCase

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