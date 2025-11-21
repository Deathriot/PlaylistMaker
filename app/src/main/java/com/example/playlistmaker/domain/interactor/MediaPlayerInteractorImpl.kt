package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.media_player.AudioPlayer
import com.example.playlistmaker.domain.media_player.MediaPlayerState

class MediaPlayerInteractorImpl(
    private val audioPlayer: AudioPlayer
) : MediaPlayerInteractor {
    private var currentState = MediaPlayerState.STATE_DEFAULT

    override fun pause() {
        audioPlayer.pause()
        currentState = MediaPlayerState.STATE_PAUSED
    }

    override fun changeState(onPlaying: () -> Unit, onPause: () -> Unit) {
        when (currentState) {
            MediaPlayerState.STATE_PLAYING -> {
                audioPlayer.pause()
                currentState = MediaPlayerState.STATE_PAUSED
                onPlaying.invoke()
            }

            MediaPlayerState.STATE_PREPARED, MediaPlayerState.STATE_PAUSED -> {
                audioPlayer.start()
                currentState = MediaPlayerState.STATE_PLAYING
                onPause.invoke()
            }

            MediaPlayerState.STATE_DEFAULT -> {

            }
        }
    }

    override fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit) {
        currentState = MediaPlayerState.STATE_PREPARED

        audioPlayer.prepare(
            path = path,
            onPrepare = onPrepare,
            onCompletion = {
                currentState = MediaPlayerState.STATE_PREPARED
                onCompletion.invoke()
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