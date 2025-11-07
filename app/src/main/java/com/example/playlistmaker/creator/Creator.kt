package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.media_player.DefaultMediaPlayer
import com.example.playlistmaker.data.network.TrackRetrofitITunesNetworkClient
import com.example.playlistmaker.data.repository.DarkThemeRepositoryImpl
import com.example.playlistmaker.data.repository.HistoryTrackRepositoryImpl
import com.example.playlistmaker.data.repository.TrackNetworkClient
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.DarkThemeRepository
import com.example.playlistmaker.domain.repository.HistoryTrackRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.use_case.GetDarkThemeUseCase
import com.example.playlistmaker.domain.use_case.GetTracksUseCase
import com.example.playlistmaker.domain.interactor.HistoryTrackInteractor
import com.example.playlistmaker.domain.interactor.HistoryTrackInteractorImpl
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.media_player.MediaPlayer
import com.example.playlistmaker.domain.use_case.SetDarkThemeUseCase
import com.example.playlistmaker.ui.app.App

object Creator {
    private lateinit var app: Application

    fun initApplication(app: Application) {
        this.app = app
    }

    fun provideGetDarkThemeUseCase(): GetDarkThemeUseCase {
        return GetDarkThemeUseCase(provideDarkThemeRepository())
    }

    fun provideSetDarkThemeUseCase(): SetDarkThemeUseCase {
        return SetDarkThemeUseCase(provideDarkThemeRepository())
    }

    fun provideGetTracksUseCase(): GetTracksUseCase {
        return GetTracksUseCase(provideTrackRepository())
    }

    fun provideHistoryTrackInteractor(): HistoryTrackInteractor {
        return HistoryTrackInteractorImpl(provideHistoryTrackRepository())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayer())
    }

    private fun provideMediaPlayer(): MediaPlayer {
        return DefaultMediaPlayer()
    }

    private fun provideTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(provideTrackNetworkClient())
    }

    private fun provideTrackNetworkClient(): TrackNetworkClient {
        return TrackRetrofitITunesNetworkClient()
    }

    private fun provideDarkThemeRepository(): DarkThemeRepository {
        return DarkThemeRepositoryImpl(provideSharedPreferences())
    }

    private fun provideHistoryTrackRepository(): HistoryTrackRepository {
        return HistoryTrackRepositoryImpl(provideSharedPreferences())
    }

    private fun provideSharedPreferences(): SharedPreferences {
        return app.getSharedPreferences(App.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }
}