package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.model.DefaultMediaPlayer
import com.example.playlistmaker.data.search.TrackNetworkClient
import com.example.playlistmaker.data.search.impl.TrackRetrofitITunesNetworkClient
import com.example.playlistmaker.data.search.itunes.ITunesApi
import com.example.playlistmaker.data.settings.impl.DarkThemeInteractorImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.data.storage.PrefsStorageClient
import com.example.playlistmaker.domain.player.model.AudioPlayer
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.settings.DarkThemeInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.storage.StorageClient
import com.example.playlistmaker.ui.app.App
import com.example.playlistmaker.ui.search.viewmodel.model.SearchConstants
import com.example.playlistmaker.ui.settings.viewmodel.model.SettingsConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    factory<StorageClient<Boolean>>(named(themeStorage)) {
        PrefsStorageClient(
            androidContext(),
            App.THEME_KEY,
            object : TypeToken<Boolean>() {}.type,
            App.APP_SHARED_PREFERENCES
        )
    }

    factory<StorageClient<ArrayList<Track>>>(named(trackStorage)) {
        PrefsStorageClient(
            androidContext(),
            App.SEARCH_HISTORY_KEY,
            object : TypeToken<ArrayList<Track>>() {}.type,
            App.APP_SHARED_PREFERENCES
        )
    }

    factory<AudioPlayer> {
        DefaultMediaPlayer()
    }

    single<TrackNetworkClient> {
        TrackRetrofitITunesNetworkClient(get())
    }

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    factory {
        Gson()
    }

    single<DarkThemeInteractor> {
        DarkThemeInteractorImpl()
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single {
        SearchConstants(androidContext())
    }

    single {
        SettingsConstants(androidContext())
    }
}