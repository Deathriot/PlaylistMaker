package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.impl.HistoryTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.impl.DarkThemeRepositoryImpl
import com.example.playlistmaker.domain.search.repository.HistoryTrackRepository
import com.example.playlistmaker.domain.search.repository.TrackRepository
import com.example.playlistmaker.domain.settings.repository.DarkThemeRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<DarkThemeRepository> {
        DarkThemeRepositoryImpl(get(named(themeStorage)))
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<HistoryTrackRepository> {
        HistoryTrackRepositoryImpl(get(named(trackStorage)))
    }
}

const val themeStorage = "THEME_STORAGE"
const val trackStorage = "TRACK_STORAGE"