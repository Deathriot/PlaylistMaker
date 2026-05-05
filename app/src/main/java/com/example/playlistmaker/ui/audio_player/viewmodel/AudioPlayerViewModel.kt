package com.example.playlistmaker.ui.audio_player.viewmodel

import android.Manifest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.media.GetAllPlaylistsUseCase
import com.example.playlistmaker.domain.player.AddTrackToPlaylistUseCase
import com.example.playlistmaker.ui.audio_player.model.AddedToPlaylistState
import com.example.playlistmaker.ui.audio_player.model.PlayerState
import com.example.playlistmaker.ui.audio_player.model.TotalPlayerState
import com.example.playlistmaker.ui.media.converter.PlaylistConverter
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.search.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.utils.services.media_player_service.MediaPlayerControl

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: TrackDetailsInfo,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase
) : ViewModel() {
    private val totalPlayerState = TotalPlayerState(PlayerState.Default(), false)

    private var _playerControl: MediaPlayerControl? = null
    private lateinit var playerControl: MediaPlayerControl

    private val playerStateLiveData = MutableLiveData(totalPlayerState)
    fun observePlayerState(): LiveData<TotalPlayerState> = playerStateLiveData

    private val playlistsLiveData = MutableLiveData<List<PlaylistDetails>>()
    fun observePlaylists(): LiveData<List<PlaylistDetails>> = playlistsLiveData

    private val isTrackAddedToPlaylist = MutableLiveData<AddedToPlaylistState>()
    fun observeIsTrackAdded(): LiveData<AddedToPlaylistState> = isTrackAddedToPlaylist

    fun prepare() {
        checkIsFavorite()
        loadPlaylists()
    }

    fun setMediaPlayerControl(playerControl: MediaPlayerControl) {
        this._playerControl = playerControl
        this.playerControl = _playerControl!!
        viewModelScope.launch {
            playerControl.getPlayerState().collect {
                totalPlayerState.playerState = it
                playerStateLiveData.postValue(totalPlayerState)
            }
        }
    }

    fun removeMediaPlayerControl() {
        _playerControl = null
    }

    fun addTrackToPlaylist(playlistId: Long, playlistName: String) {
        viewModelScope.launch {
            val addedTrack = TrackDetailsInfoMapper.mapToTrack(track)
            val isAdded = addTrackToPlaylistUseCase.execute(playlistId, addedTrack).first()

            isTrackAddedToPlaylist.postValue(
                AddedToPlaylistState(
                    playlistName,
                    isAdded
                )
            )
        }
    }

    fun changeLikeState() {
        val liked = totalPlayerState.isLiked

        viewModelScope.launch {
            val favTrack = TrackDetailsInfoMapper.mapToTrack(track)

            if (liked) {
                favoriteTracksInteractor.deleteTrack(favTrack)
            } else {
                favoriteTracksInteractor.addTrack(favTrack)
            }
            totalPlayerState.isLiked = !liked
            playerStateLiveData.postValue(totalPlayerState)
        }
    }

    fun changeState() {
        playerControl.changeState()
    }


    fun loadPlaylists() {
        viewModelScope.launch {
            getAllPlaylistsUseCase.execute().collect {
                val playlistDetails = it.map { playlist ->
                    PlaylistConverter.convertToDetails(playlist)
                }
                playlistsLiveData.postValue(playlistDetails)
            }
        }
    }

    private fun checkIsFavorite() {
        if (track.isFavorite) {
            return
        }

        viewModelScope.launch {
            favoriteTracksInteractor.getTrack(track.id).collect {
                if (it == null) {
                    totalPlayerState.isLiked = false
                    playerStateLiveData.postValue(totalPlayerState)
                } else {
                    totalPlayerState.isLiked = true
                    playerStateLiveData.postValue(totalPlayerState)
                }
            }
        }
    }
}