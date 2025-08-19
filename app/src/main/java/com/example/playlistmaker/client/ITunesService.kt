package com.example.playlistmaker.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesService {

    private val translateBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(translateBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ITunesApi::class.java)
}