package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.repository.DarkThemeRepository
import com.example.playlistmaker.domain.settings.GetDarkThemeUseCase

class GetDarkThemeUseCaseImpl(
    private val repository: DarkThemeRepository
) : GetDarkThemeUseCase {

    override fun execute(consumer: (Boolean) -> Unit) {
        consumer.invoke(repository.isDark())
    }
}