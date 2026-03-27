package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.GetAllPlaylistsUseCase
import com.example.playlistmaker.ui.media.converter.PlaylistConverter
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.model.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase
) : ViewModel() {
    init {
        loadPlaylists()
    }

    private val playlists = MutableLiveData<List<PlaylistDetails>>()
    fun observePlaylists(): LiveData<List<PlaylistDetails>> = playlists

    private val clickedPlaylist = SingleLiveEvent<Long>()
    fun observeClickedPlaylist(): LiveData<Long> = clickedPlaylist

    fun loadPlaylists() {
        viewModelScope.launch {
            getAllPlaylistsUseCase.execute().collect {
                val playlistsDto = it.map { playlist ->
                    PlaylistConverter.convertToDetails(playlist)
                }

                playlists.postValue(playlistsDto)
            }
        }
    }

    fun onPlaylistClick(playlistId: Long) {
        clickedPlaylist.postValue(playlistId)
    }
}