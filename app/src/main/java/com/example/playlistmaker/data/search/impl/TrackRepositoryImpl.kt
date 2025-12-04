package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.mapper.TrackMapper
import com.example.playlistmaker.data.search.model.TrackNetworkResponse
import com.example.playlistmaker.data.search.TrackNetworkClient
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.repository.TrackRepository

class TrackRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient
) : TrackRepository {
    private var currentTitle = ""
    private var trackList : List<Track>? = ArrayList()

    override fun getTracks(title: String, consumer : Consumer<List<Track>?>) {
        if(currentTitle == title){
            consumer.consume(Result.success(trackList))
        } else {
            currentTitle = title
            trackNetworkClient.getTracks(
                title = title,
                consumer = object : Consumer<TrackNetworkResponse?> {
                    override fun consume(data: Result<TrackNetworkResponse?>) {
                        if(data.isFailure){
                            consumer.consume(Result.failure(data.exceptionOrNull()!!))
                        } else if (data.isSuccess){
                            val tracksDto = data.getOrNull()?.tracks?.filter { it.musicUrl != null }
                            val tracks = tracksDto?.map { TrackMapper.map(it) }
                            trackList = tracks
                            consumer.consume(Result.success(tracks))
                        }
                    }
                })
        }
    }

    override fun getTrackById(id: Long): Track? {
        return trackList?.find { it.id == id }
    }
}