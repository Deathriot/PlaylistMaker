package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.converter.Formatter.formatTime
import com.example.playlistmaker.data.db.converter.Formatter.transformTimeMillisFromString
import com.example.playlistmaker.data.db.model.FavoriteTrackEntity
import com.example.playlistmaker.domain.search.model.Track

object FavoriteTrackEntityConvertor {
    fun convertToTrack(trackEntity: FavoriteTrackEntity): Track {
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
            isFavorite = true
        )
    }

    fun convertToEntity(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
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
        )
    }
}