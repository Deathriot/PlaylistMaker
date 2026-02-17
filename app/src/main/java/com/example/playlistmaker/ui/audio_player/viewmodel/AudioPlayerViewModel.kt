package com.example.playlistmaker.ui.audio_player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.ui.search.mapper.TimeFormatter.formatTime
import com.example.playlistmaker.ui.search.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: TrackDetailsInfo,
    private val playerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val playerState = MutableLiveData(MediaPlayerState.STATE_DEFAULT)
    fun observePlayerState(): LiveData<MediaPlayerState> = playerState

    private val timer = MutableLiveData(DEFAULT_TIMER_VALUE)
    fun observeTimer(): LiveData<String> = timer

    private val isLiked = MutableLiveData(track.isFavorite)
    fun observeIsLiked(): LiveData<Boolean> = isLiked

    private var timerJob: Job? = null

    fun prepare() {
        playerInteractor.prepare(path = track.musicUrl,
            onPrepare = {
                playerState.postValue(MediaPlayerState.STATE_PREPARED)
            },
            onCompletion = {
                playerState.postValue(MediaPlayerState.STATE_PREPARED)
                resetTimer()
            })

        isFavorite()
    }

    fun changeLikeState() {
        val liked = isLiked.value!!

        viewModelScope.launch {
            val favTrack = TrackDetailsInfoMapper.mapToTrack(track)

            if (liked) {
                favoriteTracksInteractor.deleteTrack(favTrack)
            } else {
                favoriteTracksInteractor.addTrack(favTrack)
            }
            isLiked.postValue(!liked)
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

    private fun isFavorite() {
        if(track.isFavorite){
            return
        }

        viewModelScope.launch {
            favoriteTracksInteractor.getTrack(track.id).collect {
                if(it == null){
                    isLiked.postValue(false)
                } else {
                    isLiked.postValue(true)
                }
            }
        }
    }

    private fun onPause() {
        playerState.postValue(MediaPlayerState.STATE_PAUSED)
        pauseTimer()
    }

    private fun onPlaying() {
        playerState.postValue(MediaPlayerState.STATE_PLAYING)
        playerState.value = MediaPlayerState.STATE_PLAYING
        startTimerUpdate()
    }

    private fun resetTimer() {
        timer.postValue(DEFAULT_TIMER_VALUE)
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            while (playerState.value == MediaPlayerState.STATE_PLAYING) {
                delay(TIMER_DELAY_MILLIS)
                val currentTime = formatTime(playerInteractor.getCurrentTrackTime())
                timer.postValue(currentTime)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        resetTimer()
    }

    companion object {
        private const val DEFAULT_TIMER_VALUE = "00:00"
        private const val TIMER_DELAY_MILLIS = 200L
    }
}