package com.example.playlistmaker.ui.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.databaseModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.useCaseModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.GetDarkThemeUseCase
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private val getDarkThemeUseCase: GetDarkThemeUseCase by inject()

    override fun onCreate() {
        super.onCreate()
        PermissionRequester.initialize(applicationContext)
        startKoin {
            androidContext(this@App.applicationContext)
            modules(
                useCaseModule,
                interactorModule,
                repositoryModule,
                viewModelModule,
                dataModule,
                databaseModule
            )
        }

        getDarkThemeUseCase.execute {
            AppCompatDelegate.setDefaultNightMode(
                if (it) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    companion object {
        const val APP_SHARED_PREFERENCES = "app_prefs"
        const val SEARCH_HISTORY_KEY = "search_history"
        const val THEME_KEY = "theme"

        const val FAVORITE_TRACKS_TABLE_NAME = "table_favorite_tracks"
        const val PLAYLIST_TRACKS_TABLE_NAME = "table_playlist_tracks"
        const val PLAYLIST_TABLE_NAME = "table_playlists"

        const val PLAYLIST_COVERS = "covers"
    }
}