package com.example.playlistmaker.ui.new_playlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.new_playlist.SavePlaylistUseCase
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.example.playlistmaker.domain.playlist.GetPlaylistUseCase
import com.example.playlistmaker.ui.new_playlist.mapper.PlaylistInfoMapper
import com.example.playlistmaker.ui.new_playlist.model.PlaylistInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val savePlaylistUseCase: SavePlaylistUseCase,
    private val getPlaylistUseCase: GetPlaylistUseCase
) : NewPlaylistViewModel(savePlaylistUseCase) {

    private lateinit var loadedPlaylist : Playlist

    private val currentPlaylist = MutableLiveData<PlaylistInfo>()
    fun observeCurrentPlaylist(): LiveData<PlaylistInfo> = currentPlaylist

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            loadedPlaylist = getPlaylistUseCase.execute(playlistId).first()
                ?: throw NullPointerException("При переходе в редактирование плейлиста плейлист не найден: id = $playlistId")

            val playlistInfo = PlaylistInfoMapper.mapToInfo(loadedPlaylist)
            currentPlaylist.postValue(playlistInfo)
        }
    }

    override fun savePlaylist(name: String, description: String) {
        val newPlaylist = loadedPlaylist.copy(name = name, description = description)

        viewModelScope.launch {
            savePlaylistUseCase.update(newPlaylist, currentTrackCover.value)
            isPlaylistSaved.postValue(true)
        }
    }
}