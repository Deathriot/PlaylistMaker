package com.example.playlistmaker.domain.playlist.navigation.impl

import com.example.playlistmaker.domain.mapper.PluralMapper
import com.example.playlistmaker.domain.playlist.navigation.SharePlaylistUseCase
import com.example.playlistmaker.domain.playlist.navigation.model.PlaylistForShare
import com.example.playlistmaker.domain.playlist.navigation.model.TrackForShare
import com.example.playlistmaker.domain.sharing.ExternalNavigator

class SharePlaylistUseCaseImpl(
    private val pluralMapper: PluralMapper,
    private val navigator: ExternalNavigator
) : SharePlaylistUseCase {
    private val sb = StringBuilder()

    override fun execute(playlist: PlaylistForShare, tracks: List<TrackForShare>) {
        setPlaylistString(playlist)
        setTracksString(tracks)
        navigator.share(sb.toString(), "Отправляем плейлист")
        println(sb.toString())
        sb.clear()
    }

    private fun setPlaylistString(playlist: PlaylistForShare) {
        sb.appendLine(playlist.name)
        if (!playlist.description.isNullOrEmpty()) {
            sb.append("${playlist.description} ")
        }
        sb.appendLine(pluralMapper.getTracksPlural(playlist.trackCount))
    }

    private fun setTracksString(tracks: List<TrackForShare>) {
        tracks.forEachIndexed { index, track ->
            sb.append("${index + 1}. ")
            sb.append("${track.artistName} - ")
            sb.append("${track.title} ")
            sb.append("(${track.time})")
            sb.appendLine()
        }
    }
}