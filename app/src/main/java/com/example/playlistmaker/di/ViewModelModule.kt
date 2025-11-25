package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audio_player.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.settings.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (musicUrl: String) ->
        AudioPlayerViewModel(musicUrl, get())
    }

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}