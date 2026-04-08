package com.example.playlistmaker.domain.new_playlist.impl

import android.net.Uri
import com.example.playlistmaker.domain.db.repository.PlaylistRepository
import com.example.playlistmaker.domain.new_playlist.SavePlaylistUseCase
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.domain.storage.FileStorageClient

class SavePlaylistUseCaseImpl(
    private val fileStorageClient: FileStorageClient,
    private val playlistRepository: PlaylistRepository
) : SavePlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        if (playlist.coverUri != null) {
            fileStorageClient.createFile(playlist.coverUri)
                .collect {
                    playlistRepository.savePlaylist(changePlaylistUri(playlist, it))
                }
        } else {
            playlistRepository.savePlaylist(playlist)
        }
    }

    override suspend fun update(playlist: Playlist, newCover: Uri?) {
        if (newCover == null) {
            playlistRepository.savePlaylist(playlist)
            return
        }

        if (playlist.coverUri == null || playlist.coverUri.toString() == "null") {
            fileStorageClient.createFile(newCover)
                .collect {
                    playlistRepository.savePlaylist(changePlaylistUri(playlist, it))
                }
        } else {
            fileStorageClient.deleteFile(playlist.coverUri)
            fileStorageClient.createFile(newCover).collect {
                playlistRepository.savePlaylist(changePlaylistUri(playlist, it))
            }
        }
    }

    private fun changePlaylistUri(playlist: Playlist, uri: Uri): Playlist {
        return playlist.copy(coverUri = uri)
    }
}