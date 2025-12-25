package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.mapper.TrackMapper
import com.example.playlistmaker.data.search.TrackNetworkClient
import com.example.playlistmaker.domain.exception.InternetErrorException
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient
) : TrackRepository {
    private var currentTitle = ""
    private var trackList: List<Track>? = ArrayList()


    override fun getTracks(title: String): Flow<Result<List<Track>?>> {
        if (currentTitle == title) {
            return flow {
                emit(Result.success(trackList))
            }
        }

        currentTitle = title

        return flow {
            val response = trackNetworkClient.getTracks(title)
            when (response.resultCode) {
                200 -> {
                    val tracksDto = response.tracks.filter { it.musicUrl != null }
                    val tracks = tracksDto.map { TrackMapper.map(it) }
                    trackList = tracks
                    emit(Result.success(tracks))
                }

                else -> {
                    emit(Result.failure(InternetErrorException()))
                }
            }
        }

    }


    override fun getTrackById(id: Long): Track? {
        return trackList?.find { it.id == id }
    }
}