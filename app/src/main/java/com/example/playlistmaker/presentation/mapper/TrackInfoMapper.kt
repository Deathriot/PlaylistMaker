package com.example.playlistmaker.presentation.mapper

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.mapper.TimeFormatter.formatTime
import com.example.playlistmaker.presentation.model.TrackInfo

object TrackInfoMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            id = track.id,
            title = track.title,
            time = formatTime(track.timeMillis),
            artistName = track.artistName,
            artworkUrl100 = track.artworkUrl100
        )
    }
}