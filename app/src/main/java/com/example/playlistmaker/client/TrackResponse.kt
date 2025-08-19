package com.example.playlistmaker.client

import com.example.playlistmaker.model.Track
import com.google.gson.annotations.SerializedName

class TrackResponse (
    @SerializedName("results") val tracks : ArrayList<Track>,
    @SerializedName("resultCount") val trackCount : Int
)