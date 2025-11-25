package com.example.playlistmaker.domain.use_case.settings

import com.example.playlistmaker.domain.repository.settings.DarkThemeRepository
import com.example.playlistmaker.domain.util.dark_theme.DarkThemeInteractor

class SetDarkThemeUseCaseImpl(
    private val repository: DarkThemeRepository,
    private val darkThemeInteractor : DarkThemeInteractor
) : SetDarkThemeUseCase {
    override fun execute(isDark: Boolean) {
        repository.saveTheme(isDark)
        darkThemeInteractor.setTheme(isDark)
    }
}