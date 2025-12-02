package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.search.HistoryTrackInteractor
import com.example.playlistmaker.domain.search.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<HistoryTrackInteractor> {
        HistoryTrackInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get(), get(), get())
    }
}