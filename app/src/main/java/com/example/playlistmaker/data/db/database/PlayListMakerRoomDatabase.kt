package com.example.playlistmaker.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.model.FavoriteTrackEntity
import com.example.playlistmaker.data.db.model.PlaylistEntity

@Database(
    version = 6,
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class
    ]
)
abstract class PlayListMakerRoomDatabase : RoomDatabase() {
    abstract fun getFavoriteTracksDao(): FavoriteTracksDao
    abstract fun getPlaylistDao(): PlaylistDao
}