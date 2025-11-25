package com.example.playlistmaker.domain.use_case.settings

interface GetDarkThemeUseCase {
    fun execute(consumer : (Boolean) -> Unit)
}