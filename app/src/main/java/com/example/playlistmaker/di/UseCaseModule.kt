package com.example.playlistmaker.di

import com.example.playlistmaker.domain.media.GetAllPlaylistsUseCase
import com.example.playlistmaker.domain.media.impl.GetAllPlaylistsUseCaseImpl
import com.example.playlistmaker.domain.new_playlist.SavePlaylistUseCase
import com.example.playlistmaker.domain.new_playlist.impl.SavePlaylistUseCaseImpl
import com.example.playlistmaker.domain.player.AddTrackToPlaylistUseCase
import com.example.playlistmaker.domain.player.impl.AddTrackToPlaylistUseCaseImpl
import com.example.playlistmaker.domain.playlist.DeletePlaylistTrackUseCase
import com.example.playlistmaker.domain.playlist.DeletePlaylistUseCase
import com.example.playlistmaker.domain.playlist.GetAllPlaylistTracksUseCase
import com.example.playlistmaker.domain.playlist.GetPlaylistTrackUseCase
import com.example.playlistmaker.domain.playlist.GetPlaylistUseCase
import com.example.playlistmaker.domain.playlist.impl.DeletePlaylistTrackUseCaseImpl
import com.example.playlistmaker.domain.playlist.impl.DeletePlaylistUseCaseImpl
import com.example.playlistmaker.domain.playlist.impl.GetAllPlaylistTracksUseCaseImpl
import com.example.playlistmaker.domain.playlist.impl.GetPlaylistTrackUseCaseImpl
import com.example.playlistmaker.domain.playlist.impl.GetPlaylistUseCaseImpl
import com.example.playlistmaker.domain.playlist.navigation.SharePlaylistUseCase
import com.example.playlistmaker.domain.playlist.navigation.impl.SharePlaylistUseCaseImpl
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
        AddTrackToPlaylistUseCaseImpl(get(), get())
    }

    single<GetPlaylistUseCase> {
        GetPlaylistUseCaseImpl(get())
    }

    single<GetAllPlaylistTracksUseCase> {
        GetAllPlaylistTracksUseCaseImpl(get())
    }

    single<GetPlaylistTrackUseCase> {
        GetPlaylistTrackUseCaseImpl(get())
    }

    single<DeletePlaylistTrackUseCase> {
        DeletePlaylistTrackUseCaseImpl(get(), get())
    }

    single<SharePlaylistUseCase> {
        SharePlaylistUseCaseImpl(get(), get())
    }

    single<DeletePlaylistUseCase> {
        DeletePlaylistUseCaseImpl(get(), get(), get())
    }
}