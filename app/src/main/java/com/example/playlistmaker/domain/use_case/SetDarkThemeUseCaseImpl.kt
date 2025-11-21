package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.DarkThemeRepository

class SetDarkThemeUseCaseImpl(
    private val repository: DarkThemeRepository
) : SetDarkThemeUseCase {
    override fun execute(isDark: Boolean, consumer: (Boolean) -> Unit) {
        repository.saveTheme(isDark)
        consumer.invoke(isDark)
    }
}