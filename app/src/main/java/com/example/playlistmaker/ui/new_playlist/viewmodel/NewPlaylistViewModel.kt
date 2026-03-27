package com.example.playlistmaker.ui.new_playlist.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.new_playlist.SavePlaylistUseCase
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(
    private val savePlaylistUseCase: SavePlaylistUseCase
) : ViewModel() {

    protected val currentTrackCover = MutableLiveData<Uri>()
    fun observeCurrentTrackCover(): LiveData<Uri> = currentTrackCover

    protected val isPlaylistSaved = MutableLiveData<Boolean>()
    fun observePlaylistSaved(): LiveData<Boolean> = isPlaylistSaved

    private val shouldShowDialog = MutableLiveData<Boolean>()
    fun observeShouldShowDialog(): LiveData<Boolean> = shouldShowDialog

    fun setTrackCover(uri: Uri?) {
        if (uri == null) {
            return
        }
        currentTrackCover.postValue(uri!!)
    }

    open fun savePlaylist(name: String, description: String) {
        val playlist = Playlist(
            name = name,
            description = description,
            coverUri = currentTrackCover.value
        )

        viewModelScope.launch {
            savePlaylistUseCase.execute(playlist)
            isPlaylistSaved.postValue(true)
        }
    }

    fun checkShouldShowDialog(name: String, description: String){
        if(name.isNotEmpty() || description.isNotEmpty() || currentTrackCover.value != null){
            shouldShowDialog.postValue(true)
        } else {
            shouldShowDialog.postValue(false)
        }
    }
}