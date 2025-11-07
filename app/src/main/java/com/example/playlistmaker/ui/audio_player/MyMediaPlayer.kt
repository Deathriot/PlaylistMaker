package com.example.playlistmaker.ui.audio_player

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.presentation.mapper.TimeFormatter.formatTime

class MyMediaPlayer(
    private val binding: ActivityAudioPlayerBinding,
    private val musicUrl: String,
    private val context: Context
) {

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())


    private val timerRunnable = object : Runnable {
        override fun run() {
            val currentTime = formatTime(mediaPlayerInteractor.getCurrentTrackTime())
            binding.playerTrackCurrentTime.text = currentTime
            handler.postDelayed(this, TIMER_DELAY_MILLIS)
        }
    }

    init {
        prepare()
    }

    private fun prepare() {
        mediaPlayerInteractor.prepare(
            path = musicUrl,
            onPrepare = {
                binding.playerPlayBtn.isEnabled = true
            },
            onCompletion = {
                resetTimer()
                binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_play_btn_83)
            }
        )
    }

    fun onClickPlayBtn() {
        mediaPlayerInteractor.changeState(
            onPlaying = {
                pause()
            },
            onPause = {
                start()
            }
        )
    }

    private fun start() {
        binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_pause_btn_83)
        handler.post(timerRunnable)
    }

    fun pause() {
        binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_play_btn_83)
        handler.removeCallbacks(timerRunnable)
    }

    fun release() {
        handler.removeCallbacks(timerRunnable)
        mediaPlayerInteractor.release()
        resetTimer()
    }

    private fun resetTimer() {
        binding.playerTrackCurrentTime.text = context.getString(R.string.default_timer_value)
        handler.removeCallbacks(timerRunnable)
    }


    companion object {
        private const val TIMER_DELAY_MILLIS = 334L
    }
}