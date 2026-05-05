package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audio_player.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.ui.media.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.ui.media.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.new_playlist.viewmodel.EditPlaylistViewModel
import com.example.playlistmaker.ui.new_playlist.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.ui.playlist.viewmodel.PlaylistViewModel
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.search.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.settings.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (track: TrackDetailsInfo) ->
        AudioPlayerViewModel(track, get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel {
        EditPlaylistViewModel(get(), get())
    }
}