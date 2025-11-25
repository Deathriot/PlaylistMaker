package com.example.playlistmaker.data.player.model

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.model.AudioPlayer

class DefaultMediaPlayer : AudioPlayer {
    private val mediaPlayer = MediaPlayer()

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
        mediaPlayer.release()
    }
}