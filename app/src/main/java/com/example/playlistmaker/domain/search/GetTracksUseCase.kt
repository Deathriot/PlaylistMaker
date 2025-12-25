package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface GetTracksUseCase {
    fun execute(title: String) : Flow<Result<List<Track>?>>

    fun getById(id: Long): Track?
}