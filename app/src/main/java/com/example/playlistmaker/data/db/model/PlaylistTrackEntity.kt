package com.example.playlistmaker.data.db.model

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.ui.app.App

@Entity(tableName = App.PLAYLIST_TRACKS_TABLE_NAME)
data class PlaylistTrackEntity(
    @PrimaryKey
    @ColumnInfo(name = "track_id")
    val id: Long,

    @ColumnInfo(name = "track_title", typeAffinity = TEXT)
    val title: String,

    @ColumnInfo(name = "track_artistName", typeAffinity = TEXT)
    val artistName: String,

    @ColumnInfo(name = "track_time", typeAffinity = TEXT)
    val time: String,

    @ColumnInfo(name = "track_artworkUrl100", typeAffinity = TEXT)
    val artworkUrl100: String,

    @ColumnInfo(name = "track_collectionName", typeAffinity = TEXT)
    val collectionName: String?,

    @ColumnInfo(name = "track_releaseDate", typeAffinity = TEXT)
    val releaseDate: String?,

    @ColumnInfo(name = "track_primaryGenreName", typeAffinity = TEXT)
    val primaryGenreName: String,

    @ColumnInfo(name = "track_country", typeAffinity = TEXT)
    val country: String,

    @ColumnInfo(name = "track_musicUrl", typeAffinity = TEXT)
    val musicUrl: String,

    @ColumnInfo("track_created_at", typeAffinity = INTEGER)
    val createdAt: Long = System.currentTimeMillis()
)