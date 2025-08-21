package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val APP_SHARED_PREFERENCES = "app_prefs"
        const val SEARCH_HISTORY_KEY = "search_history"
        const val SEARCH_NEW_TRACK_KEY = "new_track"
        private const val THEME_KEY = "theme"
    }

    private var darkTheme = false

    private lateinit var appSharedPrefs: SharedPreferences

    override fun onCreate() {
        appSharedPrefs = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE)

        darkTheme = appSharedPrefs.getBoolean(THEME_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                appSharedPrefs.edit().putBoolean(THEME_KEY, true).apply()
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                appSharedPrefs.edit().putBoolean(THEME_KEY, false).apply()
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}