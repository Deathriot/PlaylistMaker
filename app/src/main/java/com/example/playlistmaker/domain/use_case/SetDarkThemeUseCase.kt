package com.example.playlistmaker.domain.use_case

interface SetDarkThemeUseCase {
    fun execute(isDark : Boolean, consumer: (Boolean) -> Unit)
}