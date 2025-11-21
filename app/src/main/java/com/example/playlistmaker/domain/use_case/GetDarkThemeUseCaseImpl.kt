package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.DarkThemeRepository

class GetDarkThemeUseCaseImpl(
    private val repository: DarkThemeRepository
) : GetDarkThemeUseCase {

    override fun execute(consumer: (Boolean) -> Unit) {
        consumer.invoke(repository.isDark())
    }
}