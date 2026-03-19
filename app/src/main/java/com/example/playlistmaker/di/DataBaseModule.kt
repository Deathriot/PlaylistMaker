package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.data.db.database.PlayListMakerRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<PlayListMakerRoomDatabase> {
        Room.databaseBuilder(
            androidContext(), PlayListMakerRoomDatabase::class.java, DATABASE_NAME
        )
            .build()
    }
}

const val DATABASE_NAME = "playlistMaker.db"