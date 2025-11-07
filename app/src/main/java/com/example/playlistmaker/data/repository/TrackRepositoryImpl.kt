package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.model.TrackNetworkResponse
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class TrackRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient
) : TrackRepository {
    private var trackList : List<Track>? = ArrayList()

    override fun getTracks(title: String, consumer : Consumer<List<Track>?>) {

        trackNetworkClient.getTracks(
            title = title,
            consumer = object : Consumer<TrackNetworkResponse?> {
                override fun consume(data: ConsumerData<TrackNetworkResponse?>) {
                    if(data is ConsumerData.Error){
                        consumer.consume(ConsumerData.Error(data.message))
                    } else if (data is ConsumerData.Data){
                        val tracksDto = data.value?.tracks
                        val tracks = tracksDto?.map { TrackMapper.map(it) }
                        trackList = tracks
                        consumer.consume(ConsumerData.Data(tracks))
                    }
                }
            })
    }

    override fun getTrackById(id: Long): Track? {
        return trackList?.find { it.id == id }
    }
}