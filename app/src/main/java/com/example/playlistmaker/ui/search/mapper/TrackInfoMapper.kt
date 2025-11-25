package com.example.playlistmaker.ui.search.mapper

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.mapper.TimeFormatter.formatTime
import com.example.playlistmaker.ui.search.model.TrackInfo

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