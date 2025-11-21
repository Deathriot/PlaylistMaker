package com.example.playlistmaker.presentation.mapper

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.mapper.TimeFormatter.formatTime
import com.example.playlistmaker.presentation.model.TrackDetailsInfo
import java.time.ZonedDateTime

object TrackDetailsInfoMapper {
    fun map(track: Track): TrackDetailsInfo {
        return TrackDetailsInfo(
            id = track.id,
            title = track.title,
            artistName = track.artistName,
            artworkUrl512 = changeArtworkUrl(track.artworkUrl100),
            collectionName = track.collectionName,
            releaseDate = changeReleaseDate(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            musicUrl = track.musicUrl,
            time = formatTime(track.timeMillis)
        )
    }

    private fun changeArtworkUrl(artWorkUrl: String): String {
        return artWorkUrl.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun changeReleaseDate(releaseDate : String?) : String?{
        if(releaseDate == null){
            return null
        }

        val year = ZonedDateTime.parse(releaseDate).year
        return year.toString()
    }
}