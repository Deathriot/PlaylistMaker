package com.example.playlistmaker.domain.player.model

interface AudioPlayer {
    fun getCurrentTrackTime(): Int

    fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit)

    fun start()

    fun pause()

    fun release()
}