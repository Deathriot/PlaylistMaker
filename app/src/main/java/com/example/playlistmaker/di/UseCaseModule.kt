package com.example.playlistmaker.di

import com.example.playlistmaker.domain.media.GetAllPlaylistsUseCase
import com.example.playlistmaker.domain.media.impl.GetAllPlaylistsUseCaseImpl
import com.example.playlistmaker.domain.new_playlist.SavePlaylistUseCase
import com.example.playlistmaker.domain.new_playlist.impl.SavePlaylistUseCaseImpl
import com.example.playlistmaker.domain.player.AddTrackToPlaylistUseCase
import com.example.playlistmaker.domain.player.impl.AddTrackToPlaylistUseCaseImpl
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

    single<SavePlaylistUseCase> {
        SavePlaylistUseCaseImpl(get(), get())
    }

    single<GetAllPlaylistsUseCase> {
        GetAllPlaylistsUseCaseImpl(get())
    }

    single<AddTrackToPlaylistUseCase> {
        AddTrackToPlaylistUseCaseImpl(get())
    }
}