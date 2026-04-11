package com.example.playlistmaker.data.search.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.search.model.TrackITunesResponse
import com.example.playlistmaker.data.search.model.TrackNetworkResponse
import com.example.playlistmaker.data.search.TrackNetworkClient
import com.example.playlistmaker.data.search.itunes.ITunesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackRetrofitITunesNetworkClient(
    private val iTunesApi: ITunesApi,
    private val context: Context
) : TrackNetworkClient {

    override suspend fun getTracks(title: String): TrackNetworkResponse {
        return withContext(Dispatchers.IO) {
            try {
                if (!isConnected()) {
                     TrackITunesResponse(emptyList()).apply {
                        resultCode = -1
                    }
                }

                val response = iTunesApi.searchTrack(title)
                response.apply {
                    resultCode = 200
                }
            } catch (e: Throwable) {
                TrackITunesResponse(emptyList()).apply {
                    resultCode = 500
                }
            }
        }
    }

    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}