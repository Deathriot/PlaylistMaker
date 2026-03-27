package com.example.playlistmaker.domain.new_playlist

import android.net.Uri
import com.example.playlistmaker.domain.new_playlist.model.Playlist


interface SavePlaylistUseCase {
    suspend fun execute(playlist : Playlist)
    suspend fun update(playlist: Playlist, newCover: Uri?)
}