package com.example.playlistmaker.ui.audio_player.model

sealed class PlayerState(val enabled: Boolean, val state: String, val progress: String) {

    class Default : PlayerState(false, STATE_PLAY, DEFAULT_TIMER_VALUE)

    class Prepared : PlayerState(true, STATE_PLAY, DEFAULT_TIMER_VALUE)

    class Playing(progress: String) : PlayerState(true, STATE_PAUSE, progress)

    class Paused(progress: String) : PlayerState(true, STATE_PLAY, progress)

    companion object {
        private const val DEFAULT_TIMER_VALUE = "00:00"
        const val STATE_PLAY = "PLAY"
        const val STATE_PAUSE = "PAUSE"
    }
}