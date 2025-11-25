package com.example.playlistmaker.data.network.track

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ITunesService {

    private const val BASE_URL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ITunesApi = retrofit.create(ITunesApi::class.java)
}