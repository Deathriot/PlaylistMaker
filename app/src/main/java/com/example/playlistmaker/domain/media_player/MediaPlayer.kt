package com.example.playlistmaker.domain.media_player

interface MediaPlayer {
    fun getCurrentTrackTime(): Int

    fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit)

    fun start()

    fun pause()

    fun release()
}