package com.example.playlistmaker.domain.repository

interface DarkThemeRepository {
    fun isDark() : Boolean

    fun saveTheme(isDark : Boolean)
}