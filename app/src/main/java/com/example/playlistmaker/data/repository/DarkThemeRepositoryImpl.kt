package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.repository.DarkThemeRepository

class DarkThemeRepositoryImpl(
    private val prefs : SharedPreferences
) : DarkThemeRepository {
    override fun isDark(): Boolean {
        return prefs.getBoolean(THEME_KEY, false)
    }

    override fun saveTheme(isDark: Boolean) {
        prefs.edit().putBoolean(THEME_KEY, isDark).apply()
    }

    companion object{
        private const val THEME_KEY = "theme"
    }
}