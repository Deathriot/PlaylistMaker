package com.example.playlistmaker.domain.use_case.audio_player

interface MediaPlayerInteractor {
    fun pause()

    fun changeState(onPlaying: () -> Unit, onPause: () -> Unit)

    fun prepare(path: String, onPrepare: () -> Unit, onCompletion: () -> Unit)

    fun release()

    fun getCurrentTrackTime(): Int
}