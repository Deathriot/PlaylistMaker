package com.example.playlistmaker.data.media_player

import com.example.playlistmaker.domain.media_player.MediaPlayer

class DefaultMediaPlayer : MediaPlayer{
    private val mediaPlayer = android.media.MediaPlayer()

    override fun getCurrentTrackTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener{
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