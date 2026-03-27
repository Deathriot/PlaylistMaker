package com.example.playlistmaker.domain.playlist

interface DeletePlaylistTrackUseCase {
    suspend fun execute(trackId: Long, playlistId: Long)
}