package com.example.playlistmaker.data.player.model

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.model.AudioPlayer

class DefaultMediaPlayer : AudioPlayer {
    private var _mediaPlayer: MediaPlayer? = MediaPlayer()
    private val mediaPlayer = _mediaPlayer!!

    override fun getCurrentTrackTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            onPrepare.invoke()
        }

        mediaPlayer.setOnCompletionListener {
            onCompletion.invoke()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.setOnPreparedListener(null)
        mediaPlayer.setOnCompletionListener(null)
        mediaPlayer.release()
        _mediaPlayer = null
    }
}