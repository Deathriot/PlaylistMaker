package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.model.TrackITunesResponse
import com.example.playlistmaker.data.search.model.TrackNetworkResponse
import com.example.playlistmaker.data.search.TrackNetworkClient
import com.example.playlistmaker.data.search.itunes.ITunesApi
import com.example.playlistmaker.domain.consumer.Consumer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackRetrofitITunesNetworkClient(
    private val iTunesApi: ITunesApi
) : TrackNetworkClient {

    override fun getTracks(title: String, consumer: Consumer<TrackNetworkResponse?>) {
        iTunesApi.searchTrack(title).enqueue(object : Callback<TrackITunesResponse> {
            override fun onResponse(
                call: Call<TrackITunesResponse>,
                response: Response<TrackITunesResponse>
            ) {
                val trackResponse = response.body()
                trackResponse?.resultCode = response.code()
                consumer.consume(Result.success(trackResponse))
            }

            override fun onFailure(call: Call<TrackITunesResponse>, t: Throwable) {
                consumer.consume(Result.failure(t))
            }
        })
    }
}