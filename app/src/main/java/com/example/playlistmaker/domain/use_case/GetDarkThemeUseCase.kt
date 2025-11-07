package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.repository.DarkThemeRepository

class GetDarkThemeUseCase(
    private val repository: DarkThemeRepository
) {

    fun execute(consumer : (Boolean) -> Unit){
        consumer.invoke(repository.isDark())
    }
}