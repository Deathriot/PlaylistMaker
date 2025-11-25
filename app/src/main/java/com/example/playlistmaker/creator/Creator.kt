package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.data.util.media_player.DefaultMediaPlayer
import com.example.playlistmaker.data.network.track.TrackRetrofitITunesNetworkClient
import com.example.playlistmaker.data.repository.settings.DarkThemeRepositoryImpl
import com.example.playlistmaker.data.repository.search.HistoryTrackRepositoryImpl
import com.example.playlistmaker.data.repository.search.TrackNetworkClient
import com.example.playlistmaker.data.repository.search.TrackRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.data.storage.PrefsStorageClient
import com.example.playlistmaker.data.util.dark_theme.DarkThemeInteractorImpl
import com.example.playlistmaker.domain.repository.settings.DarkThemeRepository
import com.example.playlistmaker.domain.repository.search.HistoryTrackRepository
import com.example.playlistmaker.domain.repository.search.TrackRepository
import com.example.playlistmaker.domain.use_case.settings.GetDarkThemeUseCaseImpl
import com.example.playlistmaker.domain.use_case.search.GetTracksUseCaseImpl
import com.example.playlistmaker.domain.use_case.search.HistoryTrackInteractor
import com.example.playlistmaker.domain.use_case.search.HistoryTrackInteractorImpl
import com.example.playlistmaker.domain.use_case.audio_player.MediaPlayerInteractor
import com.example.playlistmaker.domain.use_case.audio_player.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.util.media_player.AudioPlayer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.storage.StorageClient
import com.example.playlistmaker.domain.use_case.settings.GetDarkThemeUseCase
import com.example.playlistmaker.domain.use_case.search.GetTracksUseCase
import com.example.playlistmaker.domain.use_case.settings.SetDarkThemeUseCase
import com.example.playlistmaker.domain.use_case.settings.SetDarkThemeUseCaseImpl
import com.example.playlistmaker.domain.use_case.settings.SettingsInteractor
import com.example.playlistmaker.domain.use_case.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.util.dark_theme.DarkThemeInteractor
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

    private fun provideDarkThemeInteractor() :DarkThemeInteractor{
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