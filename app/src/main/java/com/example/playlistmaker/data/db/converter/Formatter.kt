package com.example.playlistmaker.data.db.converter

import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object Formatter {
    fun transformTimeMillisFromString(time: String): Long {
        val (minutes, seconds) = time.split(":").map { it.toInt() }
        return (minutes.minutes + seconds.seconds).inWholeMilliseconds
    }

    fun formatTime(timeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }
}