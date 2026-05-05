package com.example.playlistmaker.di

import com.example.playlistmaker.utils.broadcast_receiver.InternetConnectionBroadcastReceiver
import org.koin.dsl.module

val broadcastReceiverModule = module {
    factory<InternetConnectionBroadcastReceiver>{
        InternetConnectionBroadcastReceiver()
    }
}