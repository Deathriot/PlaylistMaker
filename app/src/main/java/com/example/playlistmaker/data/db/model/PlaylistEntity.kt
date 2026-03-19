package com.example.playlistmaker.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.ui.app.App

@Entity(App.PLAYLIST_TABLE_NAME)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    val id: Long = 0,

    @ColumnInfo(name = "playlist_name")
    val name: String,

    @ColumnInfo(name = "playlist_description")
    val description: String?,

    @ColumnInfo(name = "playlist_cover_uri")
    val coverUri: String?,

    @ColumnInfo(name = "playlist_tracks_ids")
    val tracksIds: String,

    @ColumnInfo(name = "playlist_track_count")
    val trackCount: Int,

    @ColumnInfo("playlist_created_at", typeAffinity = ColumnInfo.INTEGER)
    val createdAt: Long = System.currentTimeMillis()
)