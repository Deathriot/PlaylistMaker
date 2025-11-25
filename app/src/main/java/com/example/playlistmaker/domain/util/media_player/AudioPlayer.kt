package com.example.playlistmaker.domain.util.media_player

interface AudioPlayer {
    fun getCurrentTrackTime(): Int

    fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit)

    fun start()

    fun pause()

    fun release()
}