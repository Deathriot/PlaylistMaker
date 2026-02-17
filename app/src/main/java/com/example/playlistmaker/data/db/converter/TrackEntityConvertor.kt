package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.model.TrackEntity
import com.example.playlistmaker.domain.search.model.Track
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object TrackEntityConvertor {
    fun convertToTrack(trackEntity: TrackEntity): Track {
        return Track(
            id = trackEntity.id,
            title = trackEntity.title,
            artistName = trackEntity.artistName,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            musicUrl = trackEntity.musicUrl,
            timeMillis = transformTimeMillisFromString(trackEntity.time),
            isFavorite = trackEntity.isFavorite
        )
    }

    fun convertToEntity(track: Track): TrackEntity {
        return TrackEntity(
            id = track.id,
            title = track.title,
            artistName = track.artistName,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            musicUrl = track.musicUrl,
            time = formatTime(track.timeMillis),
            isFavorite = track.isFavorite
        )
    }

    private fun transformTimeMillisFromString(time: String): Long {
        val (minutes, seconds) = time.split(":").map { it.toInt() }
        return (minutes.minutes + seconds.seconds).inWholeMilliseconds
    }

    private fun formatTime(timeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }
}