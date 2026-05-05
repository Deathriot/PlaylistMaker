package com.example.playlistmaker.utils.services.media_player_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.ui.audio_player.model.PlayerState
import com.example.playlistmaker.ui.search.mapper.TimeFormatter.formatTime
import com.example.playlistmaker.utils.services.media_player_service.model.ServiceTrackData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MediaPlayerService : Service(), MediaPlayerControl {
    private lateinit var trackData: ServiceTrackData

    private val binder = MediaPlayerServiceBinder()
    private val mediaPlayerInteractor: MediaPlayerInteractor by inject()
    private var timerJob: Job? = null

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private val playerState = _playerState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBind(intent: Intent?): IBinder {
        trackData = intent?.getParcelableExtra(SERVICE_TRACK_ARGS, ServiceTrackData::class.java)!!
        mediaPlayerInteractor.prepare(
            path = trackData.songUrl,

            onPrepare = {
                _playerState.value = PlayerState.Prepared()
            },
            onCompletion = {
                pauseTimer()
                _playerState.value = PlayerState.Prepared()
                stopForeGround()
            }
        )
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        try {
            createNotificationChannel()
        } catch (e: Throwable) {
            Log.d("Music Service", "Создать канал не удалось, нет разрешения", e)
        }
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun changeState() {
        mediaPlayerInteractor.changeState(
            onPlaying = {
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
                startTimer()
            },
            onPause = {
                _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
                pauseTimer()
            }
        )
    }

    fun releasePlayer() {
        pauseTimer()
        _playerState.value = PlayerState.Default()
        mediaPlayerInteractor.release()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (playerState.value is PlayerState.Playing) {
                delay(TIMER_DELAY_MILLIS)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun getCurrentPlayerPosition(): String {
        return formatTime(mediaPlayerInteractor.getCurrentTrackTime())
    }

    inner class MediaPlayerServiceBinder : Binder() {
        fun getService() = this@MediaPlayerService
    }

    fun startForeground() {
        if(playerState.value is PlayerState.Playing){
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        }
    }

    fun stopForeGround() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setShowBadge(false)
            setVibrationPattern(longArrayOf(0L, 0L))
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentText(trackData.info)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    companion object {
        private const val SERVICE_NOTIFICATION_ID = 100
        private const val NOTIFICATION_CHANNEL_ID = "media_player_service_channel"

        private const val TIMER_DELAY_MILLIS = 200L

        const val SERVICE_TRACK_ARGS = "service_track_args"
    }
}