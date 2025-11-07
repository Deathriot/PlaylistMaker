package com.example.playlistmaker.data.model

import com.google.gson.annotations.SerializedName

class TrackITunesResponse(
    @SerializedName("results")
    override val tracks: List<TrackITunesDto>
) : TrackNetworkResponse()