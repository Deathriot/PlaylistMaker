package com.example.playlistmaker.ui.audio_player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.media.GetAllPlaylistsUseCase
import com.example.playlistmaker.domain.player.AddTrackToPlaylistUseCase
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.ui.audio_player.model.AddedToPlaylistState
import com.example.playlistmaker.ui.audio_player.model.PlayerState
import com.example.playlistmaker.ui.media.converter.PlaylistConverter
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.search.mapper.TimeFormatter.formatTime
import com.example.playlistmaker.ui.search.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: TrackDetailsInfo,
    private val playerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase
) : ViewModel() {
    private val playerState = PlayerState(
        MediaPlayerState.STATE_DEFAULT,
        DEFAULT_TIMER_VALUE,
        track.isFavorite
    )

    private val playerStateLiveData = MutableLiveData(playerState)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val playlistsLiveData = MutableLiveData<List<PlaylistDetails>>()
    fun observePlaylists(): LiveData<List<PlaylistDetails>> = playlistsLiveData

    private val isTrackAddedToPlaylist = MutableLiveData<AddedToPlaylistState>()
    fun observeIsTrackAdded(): LiveData<AddedToPlaylistState> = isTrackAddedToPlaylist

    private var timerJob: Job? = null

    fun prepare() {
        playerInteractor.prepare(path = track.musicUrl,
            onPrepare = {
                playerState.mediaState = MediaPlayerState.STATE_PREPARED
                playerStateLiveData.postValue(playerState)
            },
            onCompletion = {
                timerJob?.cancel()
                playerState.mediaState = MediaPlayerState.STATE_PREPARED
                playerState.timer = DEFAULT_TIMER_VALUE
                playerStateLiveData.postValue(playerState)
            })

        checkIsFavorite()
        loadPlaylists()
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
        val liked = playerState.isLiked

        viewModelScope.launch {
            val favTrack = TrackDetailsInfoMapper.mapToTrack(track)

            if (liked) {
                favoriteTracksInteractor.deleteTrack(favTrack)
            } else {
                favoriteTracksInteractor.addTrack(favTrack)
            }
            playerState.isLiked = !liked
            playerStateLiveData.postValue(playerState)
        }
    }

    fun changeState() {
        playerInteractor.changeState(
            onPlaying = this::onPlaying,
            onPause = this::onPause
        )
    }

    fun pause() {
        playerInteractor.pause()
        onPause()
    }

    fun release() {
        playerInteractor.release()
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
                    playerState.isLiked = false
                    playerStateLiveData.postValue(playerState)
                } else {
                    playerState.isLiked = true
                    playerStateLiveData.postValue(playerState)
                }
            }
        }
    }

    private fun onPause() {
        playerState.mediaState = MediaPlayerState.STATE_PAUSED
        playerStateLiveData.postValue(playerState)
        pauseTimer()
    }

    private fun onPlaying() {
        playerState.mediaState = MediaPlayerState.STATE_PLAYING
        playerStateLiveData.postValue(playerState)
        playerStateLiveData.value = playerState
        startTimerUpdate()
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            while (playerStateLiveData.value?.mediaState == MediaPlayerState.STATE_PLAYING) {
                delay(TIMER_DELAY_MILLIS)
                val currentTime = formatTime(playerInteractor.getCurrentTrackTime())
                playerState.timer = currentTime
                playerStateLiveData.postValue(playerState)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    companion object {
        private const val DEFAULT_TIMER_VALUE = "00:00"
        private const val TIMER_DELAY_MILLIS = 200L
    }
}