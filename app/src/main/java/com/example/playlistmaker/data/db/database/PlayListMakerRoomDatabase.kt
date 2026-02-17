package com.example.playlistmaker.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.data.db.model.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
abstract class PlayListMakerRoomDatabase : RoomDatabase() {
    abstract fun getFavoriteTracksDao(): FavoriteTracksDao
}