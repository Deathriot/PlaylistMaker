package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.model.Track

interface GetTracksUseCase {
    fun execute(title: String, consumer: Consumer<List<Track>?>)

    fun getById(id: Long): Track?
}