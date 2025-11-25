package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.repository.DarkThemeRepository
import com.example.playlistmaker.domain.settings.SetDarkThemeUseCase
import com.example.playlistmaker.domain.settings.DarkThemeInteractor

class SetDarkThemeUseCaseImpl(
    private val repository: DarkThemeRepository,
    private val darkThemeInteractor : DarkThemeInteractor
) : SetDarkThemeUseCase {
    override fun execute(isDark: Boolean) {
        repository.saveTheme(isDark)
        darkThemeInteractor.setTheme(isDark)
    }
}