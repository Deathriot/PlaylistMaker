package com.example.playlistmaker.data.network.track

import com.example.playlistmaker.data.model.TrackITunesResponse
import com.example.playlistmaker.data.model.TrackNetworkResponse
import com.example.playlistmaker.data.repository.search.TrackNetworkClient
import com.example.playlistmaker.domain.consumer.Consumer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackRetrofitITunesNetworkClient : TrackNetworkClient {

    override fun getTracks(title: String, consumer: Consumer<TrackNetworkResponse?>) {
        ITunesService.service.searchTrack(title).enqueue(object : Callback<TrackITunesResponse> {
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