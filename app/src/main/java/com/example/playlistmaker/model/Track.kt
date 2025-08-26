package com.example.playlistmaker.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
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
    val timeMillis: Long,

    @SerializedName("collectionName")
    val collectionName: String?,

    @SerializedName("releaseDate")
    val releaseDate: String?,

    @SerializedName("primaryGenreName")
    val primaryGenreName: String,

    @SerializedName("country")
    val country: String
) : Parcelable {

    fun setTime() {
        time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }
}