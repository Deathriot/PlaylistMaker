package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.data.player.model.DefaultMediaPlayer
import com.example.playlistmaker.data.search.impl.TrackRetrofitITunesNetworkClient
import com.example.playlistmaker.data.settings.impl.DarkThemeRepositoryImpl
import com.example.playlistmaker.data.search.impl.HistoryTrackRepositoryImpl
import com.example.playlistmaker.data.search.TrackNetworkClient
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.data.storage.PrefsStorageClient
import com.example.playlistmaker.data.settings.impl.DarkThemeInteractorImpl
import com.example.playlistmaker.domain.settings.repository.DarkThemeRepository
import com.example.playlistmaker.domain.search.repository.HistoryTrackRepository
import com.example.playlistmaker.domain.search.repository.TrackRepository
import com.example.playlistmaker.domain.settings.impl.GetDarkThemeUseCaseImpl
import com.example.playlistmaker.domain.search.impl.GetTracksUseCaseImpl
import com.example.playlistmaker.domain.search.HistoryTrackInteractor
import com.example.playlistmaker.domain.search.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.player.model.AudioPlayer
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.storage.StorageClient
import com.example.playlistmaker.domain.settings.GetDarkThemeUseCase
import com.example.playlistmaker.domain.search.GetTracksUseCase
import com.example.playlistmaker.domain.settings.SetDarkThemeUseCase
import com.example.playlistmaker.domain.settings.impl.SetDarkThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.DarkThemeInteractor
import com.example.playlistmaker.ui.app.App
import com.google.gson.reflect.TypeToken

object Creator {
    private lateinit var app: Application

    fun initApplication(app: Application) {
        this.app = app
    }

    fun provideGetDarkThemeUseCase(): GetDarkThemeUseCase {
        return GetDarkThemeUseCaseImpl(provideDarkThemeRepository())
    }

    fun provideGetTracksUseCase(): GetTracksUseCase {
        return GetTracksUseCaseImpl(provideTrackRepository())
    }

    fun provideHistoryTrackInteractor(): HistoryTrackInteractor {
        return HistoryTrackInteractorImpl(provideHistoryTrackRepository())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayer())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(
            provideExternalNavigator(),
            provideSetDarkThemeUseCase(),
            provideGetDarkThemeUseCase()
        )
    }

    private fun provideSetDarkThemeUseCase(): SetDarkThemeUseCase {
        return SetDarkThemeUseCaseImpl(provideDarkThemeRepository(), provideDarkThemeInteractor())
    }

    private fun provideDarkThemeInteractor() : DarkThemeInteractor {
        return DarkThemeInteractorImpl()
    }
    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(app.applicationContext)
    }

    private fun provideMediaPlayer(): AudioPlayer {
        return DefaultMediaPlayer()
    }

    private fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(provideTrackNetworkClient())
    }

    private fun provideTrackNetworkClient(): TrackNetworkClient {
        return TrackRetrofitITunesNetworkClient()
    }

    private fun provideDarkThemeRepository(): DarkThemeRepository {
        return DarkThemeRepositoryImpl(provideDarkThemeStorageClient())
    }

    private fun provideHistoryTrackRepository(): HistoryTrackRepository {
        return HistoryTrackRepositoryImpl(provideHistoryStorageClient())
    }

    private fun provideHistoryStorageClient(): StorageClient<ArrayList<Track>> {
        return PrefsStorageClient(
            context = app,
            dataKey = App.SEARCH_HISTORY_KEY,
            type = object : TypeToken<ArrayList<Track>>() {}.type,
            prefsKey = App.APP_SHARED_PREFERENCES
        )
    }

    private fun provideDarkThemeStorageClient(): StorageClient<Boolean> {
        return PrefsStorageClient(
            context = app,
            dataKey = App.THEME_KEY,
            type = object : TypeToken<Boolean>() {}.type,
            prefsKey = App.APP_SHARED_PREFERENCES
        )
    }
}