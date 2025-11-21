package com.example.playlistmaker.domain.use_case

interface GetDarkThemeUseCase {
    fun execute(consumer : (Boolean) -> Unit)
}