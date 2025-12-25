package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.model.TrackITunesResponse
import com.example.playlistmaker.data.search.model.TrackNetworkResponse
import com.example.playlistmaker.data.search.TrackNetworkClient
import com.example.playlistmaker.data.search.itunes.ITunesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackRetrofitITunesNetworkClient(
    private val iTunesApi: ITunesApi
) : TrackNetworkClient {

    override suspend fun getTracks(title: String): TrackNetworkResponse {
        return withContext(Dispatchers.IO){
            try{
                val response = iTunesApi.searchTrack(title)
                response.apply {
                    resultCode = 200
                }
            } catch (e : Throwable){
                TrackITunesResponse(emptyList()).apply {
                    resultCode = 500
                }
            }
        }
    }
}