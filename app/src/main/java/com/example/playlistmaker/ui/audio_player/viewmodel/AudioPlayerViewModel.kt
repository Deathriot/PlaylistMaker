package com.example.playlistmaker.ui.audio_player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.ui.search.mapper.TimeFormatter.formatTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val musicUrl: String,
    private val playerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val playerState = MutableLiveData(MediaPlayerState.STATE_DEFAULT)
    fun observePlayerState(): LiveData<MediaPlayerState> = playerState

    private val timer = MutableLiveData(DEFAULT_TIMER_VALUE)
    fun observeTimer(): LiveData<String> = timer

    private var timerJob : Job? = null
    fun prepare() {
        playerInteractor.prepare(path = musicUrl,
            onPrepare = {
                playerState.postValue(MediaPlayerState.STATE_PREPARED)
            },
            onCompletion = {
                playerState.postValue(MediaPlayerState.STATE_PREPARED)
                resetTimer()
            })
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

    fun release(){
        playerInteractor.release()
    }

    private fun onPause() {
        playerState.postValue(MediaPlayerState.STATE_PAUSED)
        pauseTimer()
    }

    private fun onPlaying() {
        playerState.postValue(MediaPlayerState.STATE_PLAYING)
        // По поводу нижней строчке написал в комментарии к пулреквесту
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
            while (playerState.value == MediaPlayerState.STATE_PLAYING){
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