package com.example.playlistmaker.ui.audio_player.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.ui.search.mapper.TimeFormatter.formatTime


class AudioPlayerViewModel(
    private val musicUrl: String,
    private val playerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val playerState = MutableLiveData(MediaPlayerState.STATE_DEFAULT)
    fun observePlayerState(): LiveData<MediaPlayerState> = playerState

    private val timer = MutableLiveData(DEFAULT_TIMER_VALUE)
    fun observeTimer(): LiveData<String> = timer

    private val timerRunnable = Runnable { startTimerUpdate() }

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

    private fun onPause() {
        playerState.postValue(MediaPlayerState.STATE_PAUSED)
        pauseTimer()
    }

    private fun onPlaying() {
        playerState.postValue(MediaPlayerState.STATE_PLAYING)
        startTimerUpdate()
    }

    private fun resetTimer() {
        timer.postValue(DEFAULT_TIMER_VALUE)
        handler.removeCallbacks(timerRunnable)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun startTimerUpdate() {
        val currentTime = formatTime(playerInteractor.getCurrentTrackTime())
        timer.postValue(currentTime)
        handler.postDelayed(timerRunnable, TIMER_DELAY_MILLIS)
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