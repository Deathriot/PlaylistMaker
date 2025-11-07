package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.DarkThemeRepository

class SetDarkThemeUseCase(
    private val repository: DarkThemeRepository
) {
    fun execute(isDark : Boolean, consumer: (Boolean) -> Unit){
        repository.saveTheme(isDark)
        consumer.invoke(isDark)
    }
}