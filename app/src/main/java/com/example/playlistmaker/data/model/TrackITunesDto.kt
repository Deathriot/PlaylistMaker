package com.example.playlistmaker.data.model

import com.google.gson.annotations.SerializedName

class TrackITunesDto (
    @SerializedName("trackId")
    override val id: Long,

    @SerializedName("trackName")
    override val title: String,

    @SerializedName("artistName")
    override val artistName: String,

    @SerializedName("artworkUrl100")
    override val artworkUrl100: String,

    @SerializedName("trackTimeMillis")
    override val timeMillis: Long,

    @SerializedName("collectionName")
    override val collectionName: String?,

    @SerializedName("releaseDate")
    override val releaseDate: String?,

    @SerializedName("primaryGenreName")
    override val primaryGenreName: String,

    @SerializedName("country")
    override val country: String,

    @SerializedName("previewUrl")
    override val musicUrl : String
): TrackNetworkDto()

