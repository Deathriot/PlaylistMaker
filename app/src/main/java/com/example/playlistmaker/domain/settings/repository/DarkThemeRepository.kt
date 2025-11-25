package com.example.playlistmaker.domain.settings.repository

interface DarkThemeRepository {
    fun isDark() : Boolean

    fun saveTheme(isDark : Boolean)
}