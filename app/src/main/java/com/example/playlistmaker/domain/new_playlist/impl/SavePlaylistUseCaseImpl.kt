package com.example.playlistmaker.domain.new_playlist.impl

import android.net.Uri
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.new_playlist.SavePlaylistUseCase
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.domain.storage.FileStorageClient
import java.util.UUID

class SavePlaylistUseCaseImpl(
    private val fileStorageClient: FileStorageClient,
    private val playlistRepository: PlaylistRepository
) : SavePlaylistUseCase {
    override suspend fun execute(playList: Playlist) {
        if (playList.coverUri != null) {
            val coverFileName = UUID.randomUUID().toString()
            fileStorageClient.createFile(coverFileName, playList.coverUri)
                .collect {
                    playlistRepository.savePlaylist(changePlaylistUri(playList, it))
                }
        } else {
            playlistRepository.savePlaylist(playList)
        }
    }

    private fun changePlaylistUri(playlist: Playlist, uri: Uri): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = uri,
            tracksIds = playlist.tracksIds,
            trackCount = playlist.trackCount
        )
    }
}