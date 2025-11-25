package com.example.playlistmaker.domain.settings

interface GetDarkThemeUseCase {
    fun execute(consumer : (Boolean) -> Unit)
}