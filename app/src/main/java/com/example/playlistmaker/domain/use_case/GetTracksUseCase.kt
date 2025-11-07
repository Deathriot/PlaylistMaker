package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class GetTracksUseCase(
    private val repository: TrackRepository
) {
    fun execute(title: String, consumer : Consumer<List<Track>?>) {
        repository.getTracks(
            title = title,
            consumer = consumer)
    }

    fun getById(id: Long) : Track?{
        return repository.getTrackById(id)
    }
}