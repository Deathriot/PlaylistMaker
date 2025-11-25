package com.example.playlistmaker.ui.search.mapper

import java.text.SimpleDateFormat
import java.util.Locale

object TimeFormatter {
    fun formatTime(timeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }

    fun formatTime(timeMillis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }
}