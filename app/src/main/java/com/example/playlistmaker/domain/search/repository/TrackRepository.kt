package com.example.playlistmaker.domain.search.repository

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTracks(title : String) : Flow<Result<List<Track>?>>

    fun getTrackById(id: Long) : Track?
}