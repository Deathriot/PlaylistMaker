package com.example.playlistmaker.ui.playlist.mapper

import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.ui.playlist.model.PlaylistDetailsInfo
import java.text.SimpleDateFormat
import java.util.Locale

object PlaylistDetailsInfoMapper {
    fun mapToDetailsInfo(playlist: Playlist, totalMillis: Long): PlaylistDetailsInfo {
        return PlaylistDetailsInfo(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            tracksIds = playlist.tracksIds,
            trackCount = playlist.trackCount,
            totalDuration = convertTotalTime(totalMillis)
        )
    }

    fun mapToPlaylist(playlistInfo: PlaylistDetailsInfo) : Playlist{
        return Playlist(
            id = playlistInfo.id,
            name = playlistInfo.name,
            description = playlistInfo.description,
            coverUri = playlistInfo.coverUri,
            tracksIds = playlistInfo.tracksIds,
            trackCount = playlistInfo.trackCount
        )
    }

    private fun convertTotalTime(totalMillis: Long): Int {
        return SimpleDateFormat("mm", Locale.getDefault()).format(totalMillis).toInt()
    }
}