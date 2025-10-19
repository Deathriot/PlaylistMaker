package com.example.playlistmaker.util

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MyMediaPlayer(
    private val binding: ActivityAudioPlayerBinding,
    private val track: Track
) {

    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private var playerState = STATE_DEFAULT

    private val timerRunnable = object : Runnable {
        override fun run() {
            val currentTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            binding.playerTrackCurrentTime.text = currentTime
            handler.postDelayed(this, TIMER_DELAY_MILLIS)
        }
    }

    init {
        prepare()
    }

    private fun prepare() {
        mediaPlayer.setDataSource(track.musicUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            binding.playerPlayBtn.isEnabled = true
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            resetTimer()
            binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_play_btn_83)
        }
    }

    fun onClickPlayBtn(){
        when(playerState) {
            STATE_PLAYING -> {
                pause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                start()
            }
        }
    }

    fun start() {
        binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_pause_btn_83)
        handler.post(timerRunnable)
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    fun pause() {
        binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_play_btn_83)
        handler.removeCallbacks(timerRunnable)
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    fun release() {
        handler.removeCallbacks(timerRunnable)
        mediaPlayer.release()
        playerState = STATE_DEFAULT
        resetTimer()
    }

    private fun resetTimer() {
        binding.playerTrackCurrentTime.text = "0:00"
        handler.removeCallbacks(timerRunnable)
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TIMER_DELAY_MILLIS = 334L
    }
}