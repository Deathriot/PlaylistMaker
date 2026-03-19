package com.example.playlistmaker.ui.audio_player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.ui.audio_player.model.PlayerState
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
    private val playerState = createPlayerState()

    private val playerStateLiveData = MutableLiveData(playerState)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private var timerJob: Job? = null

    fun prepare() {
        playerInteractor.prepare(path = track.musicUrl,
            onPrepare = {
                playerState.mediaState = MediaPlayerState.STATE_PREPARED
                playerStateLiveData.postValue(playerState)
            },
            onCompletion = {
                playerState.mediaState = MediaPlayerState.STATE_PREPARED
                playerStateLiveData.postValue(playerState)
                resetTimer()
            })

        isFavorite()
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

    private fun createPlayerState(): PlayerState {
        return PlayerState(
            MediaPlayerState.STATE_DEFAULT,
            DEFAULT_TIMER_VALUE,
            track.isFavorite
        )
    }

    private fun isFavorite() {
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

    private fun resetTimer() {
        playerState.timer = DEFAULT_TIMER_VALUE
        playerStateLiveData.postValue(playerState)
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
        resetTimer()
    }

    companion object {
        private const val DEFAULT_TIMER_VALUE = "00:00"
        private const val TIMER_DELAY_MILLIS = 200L
    }
}