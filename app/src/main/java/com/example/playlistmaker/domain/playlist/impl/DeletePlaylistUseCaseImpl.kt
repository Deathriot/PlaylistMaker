package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.db.repository.PlaylistRepository
import com.example.playlistmaker.domain.db.repository.PlaylistTrackRepository
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.domain.playlist.DeletePlaylistUseCase
import com.example.playlistmaker.domain.storage.FileStorageClient

class DeletePlaylistUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistTracRepository: PlaylistTrackRepository,
    private val fileStorageClient: FileStorageClient
) : DeletePlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        fileStorageClient.deleteFile(playlist.coverUri)
        playlistTracRepository.deleteAllTracksFromPlaylist(playlist.id)
        playlistRepository.deletePlaylist(playlist.id)
    }
}