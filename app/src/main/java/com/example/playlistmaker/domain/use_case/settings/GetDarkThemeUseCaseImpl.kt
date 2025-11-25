package com.example.playlistmaker.domain.use_case.settings

import com.example.playlistmaker.domain.repository.settings.DarkThemeRepository

class GetDarkThemeUseCaseImpl(
    private val repository: DarkThemeRepository
) : GetDarkThemeUseCase {

    override fun execute(consumer: (Boolean) -> Unit) {
        consumer.invoke(repository.isDark())
    }
}