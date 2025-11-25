package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.GetTracksUseCase
import com.example.playlistmaker.domain.search.impl.GetTracksUseCaseImpl
import com.example.playlistmaker.domain.settings.GetDarkThemeUseCase
import com.example.playlistmaker.domain.settings.SetDarkThemeUseCase
import com.example.playlistmaker.domain.settings.impl.GetDarkThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SetDarkThemeUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<GetDarkThemeUseCase> {
        GetDarkThemeUseCaseImpl(get())
    }

    single<GetTracksUseCase> {
        GetTracksUseCaseImpl(get())
    }

    single<SetDarkThemeUseCase> {
        SetDarkThemeUseCaseImpl(get(), get())
    }
}