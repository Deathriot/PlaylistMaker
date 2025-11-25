package com.example.playlistmaker.data.search.mapper

import com.example.playlistmaker.data.search.model.TrackNetworkDto
import com.example.playlistmaker.domain.search.model.Track

object TrackMapper {
    fun map(trackDto : TrackNetworkDto) : Track {
        return Track(
            id = trackDto.id,
            title = trackDto.title,
            artistName = trackDto.artistName,
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            musicUrl = trackDto.musicUrl,
            timeMillis = trackDto.timeMillis
        )
    }
}