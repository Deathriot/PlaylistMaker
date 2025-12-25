package com.example.playlistmaker.data.search.itunes

import com.example.playlistmaker.data.search.model.TrackITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): TrackITunesResponse
}