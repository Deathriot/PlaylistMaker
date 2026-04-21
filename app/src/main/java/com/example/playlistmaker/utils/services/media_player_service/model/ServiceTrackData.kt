package com.example.playlistmaker.utils.services.media_player_service.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceTrackData(
    val info : String,
    val songUrl: String
) : Parcelable