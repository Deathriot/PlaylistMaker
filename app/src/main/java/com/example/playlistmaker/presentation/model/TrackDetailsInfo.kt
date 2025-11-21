package com.example.playlistmaker.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackDetailsInfo(
    val id: Long,

    val title: String,

    val artistName: String,

    var time: String,

    val artworkUrl512: String,

    val collectionName: String?,

    val releaseDate: String?,

    val primaryGenreName: String,

    val country: String,

    val musicUrl: String
) : Parcelable