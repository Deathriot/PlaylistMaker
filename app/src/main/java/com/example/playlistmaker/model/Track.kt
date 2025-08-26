package com.example.playlistmaker.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    @SerializedName("trackId")
    val id: Long,

    @SerializedName("trackName")
    val title: String,

    @SerializedName("artistName")
    val artistName: String,

    var time: String?,

    @SerializedName("artworkUrl100")
    val artworkUrl100: String,

    @SerializedName("trackTimeMillis")
    val timeMillis: Long?
) {
    fun setTime() {
        time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }
}