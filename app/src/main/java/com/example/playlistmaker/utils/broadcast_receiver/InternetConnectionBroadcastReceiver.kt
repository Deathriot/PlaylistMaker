package com.example.playlistmaker.utils.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.TrackNetworkClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InternetConnectionBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val networkClient: TrackNetworkClient by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ConnectivityManager.CONNECTIVITY_ACTION) {
            return
        }

        if (!networkClient.isConnected()) {
            val text = context?.getString(R.string.no_internet_connection)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}