package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.AudioPlayer
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class MediaPlayerInteractorImpl(
    private val audioPlayer: AudioPlayer
) : MediaPlayerInteractor {
    private var currentState = MediaPlayerState.STATE_DEFAULT

    override fun pause() {
        currentState = MediaPlayerState.STATE_PAUSED
        audioPlayer.pause()
    }

    override fun changeState(onPlaying: () -> Unit, onPause: () -> Unit) {
        when (currentState) {
            MediaPlayerState.STATE_PLAYING -> {
                audioPlayer.pause()
                currentState = MediaPlayerState.STATE_PAUSED
                onPause.invoke()
            }

            MediaPlayerState.STATE_PREPARED, MediaPlayerState.STATE_PAUSED -> {
                audioPlayer.start()
                currentState = MediaPlayerState.STATE_PLAYING
                onPlaying.invoke()
            }

            MediaPlayerState.STATE_DEFAULT -> {

            }
        }
    }

    override fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit) {
        audioPlayer.prepare(
            path = path,
            onPrepare = {
                onPrepare.invoke()
                currentState = MediaPlayerState.STATE_PREPARED
            },
            onCompletion = {
                onCompletion.invoke()
                currentState = MediaPlayerState.STATE_PREPARED
            }
        )
    }

    override fun release() {
        audioPlayer.release()
        currentState = MediaPlayerState.STATE_DEFAULT
    }

    override fun getCurrentTrackTime(): Int {
        return audioPlayer.getCurrentTrackTime()
    }
}