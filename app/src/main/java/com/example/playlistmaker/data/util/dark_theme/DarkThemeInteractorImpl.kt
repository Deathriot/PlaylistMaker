package com.example.playlistmaker.data.util.dark_theme

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.util.dark_theme.DarkThemeInteractor

class DarkThemeInteractorImpl : DarkThemeInteractor {
    override fun setTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}