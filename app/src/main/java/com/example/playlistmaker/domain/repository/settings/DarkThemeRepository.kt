package com.example.playlistmaker.domain.repository.settings

interface DarkThemeRepository {
    fun isDark() : Boolean

    fun saveTheme(isDark : Boolean)
}