package com.example.playlistmaker.ui.search.mapper

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.mapper.TimeFormatter.formatTime
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object TrackDetailsInfoMapper {
    fun mapToTrackDetailsInfo(track: Track): TrackDetailsInfo {
        return TrackDetailsInfo(
            id = track.id,
            title = track.title,
            artistName = track.artistName,
            artworkUrl512 = changeArtworkUrlTo512(track.artworkUrl100),
            collectionName = track.collectionName,
            releaseDate = changeReleaseDate(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            musicUrl = track.musicUrl,
            time = formatTime(track.timeMillis),
            isFavorite = track.isFavorite
        )
    }

    fun mapFromFavoriteTrackToTrackDetailsInfo(track:Track) : TrackDetailsInfo{
        return TrackDetailsInfo(
            id = track.id,
            title = track.title,
            artistName = track.artistName,
            artworkUrl512 = changeArtworkUrlTo512(track.artworkUrl100),
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            musicUrl = track.musicUrl,
            time = formatTime(track.timeMillis),
            isFavorite = track.isFavorite
        )
    }

    fun mapToTrack(trackDetailsInfo: TrackDetailsInfo): Track {
        return Track(
            id = trackDetailsInfo.id,
            title = trackDetailsInfo.title,
            artistName = trackDetailsInfo.artistName,
            artworkUrl100 = changeArtworkUrlTo100(trackDetailsInfo.artworkUrl512),
            collectionName = trackDetailsInfo.collectionName,
            releaseDate = trackDetailsInfo.releaseDate,
            primaryGenreName = trackDetailsInfo.primaryGenreName,
            country = trackDetailsInfo.country,
            musicUrl = trackDetailsInfo.musicUrl,
            timeMillis = transformTimeMillisFromString(trackDetailsInfo.time),
            isFavorite = trackDetailsInfo.isFavorite
        )
    }

    private fun changeArtworkUrlTo512(artWorkUrl: String): String {
        return artWorkUrl.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun changeArtworkUrlTo100(artWorkUrl: String): String {
        return artWorkUrl.replaceAfterLast('/', "100x100bb.jpg")
    }

    private fun transformTimeMillisFromString(time: String): Long {
        val (minutes, seconds) = time.split(":").map { it.toInt() }
        return (minutes.minutes + seconds.seconds).inWholeMilliseconds
    }

    private fun changeReleaseDate(releaseDate: String?): String? {
        if (releaseDate == null) {
            return null
        }

        println(releaseDate)
        val year = ZonedDateTime.parse(releaseDate).year
        return year.toString()
    }
}