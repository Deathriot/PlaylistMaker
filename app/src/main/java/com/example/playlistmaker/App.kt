package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    private val APP_SHARED_PREFERENCES = "app_prefs"
    private val THEME_KEY = "theme"
    private lateinit var appSharedPrefs: SharedPreferences

    override fun onCreate() {
        appSharedPrefs = getSharedPreferences(APP_SHARED_PREFERENCES, MODE_PRIVATE)

        val theme = appSharedPrefs.getString(THEME_KEY, Theme.DAY.theme)
        darkTheme = if (theme.equals(Theme.DAY.theme)) {
            false
        } else true

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
                appSharedPrefs.edit().putString(THEME_KEY, Theme.NIGHT.theme).apply()
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                appSharedPrefs.edit().putString(THEME_KEY, Theme.DAY.theme).apply()
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    enum class Theme(val theme: String) {
        DAY("day"),
        NIGHT("night")
    }
}