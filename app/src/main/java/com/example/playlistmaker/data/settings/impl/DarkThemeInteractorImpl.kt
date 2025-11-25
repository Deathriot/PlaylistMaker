package com.example.playlistmaker.data.settings.impl

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.DarkThemeInteractor

class DarkThemeInteractorImpl : DarkThemeInteractor {
    override fun setTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}