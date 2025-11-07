package com.example.playlistmaker.ui.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        val getDarkThemeUseCase = Creator.provideGetDarkThemeUseCase()

        getDarkThemeUseCase.execute {
            AppCompatDelegate.setDefaultNightMode(
                if (it) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    companion object {
        const val APP_SHARED_PREFERENCES = "app_prefs"
    }
}