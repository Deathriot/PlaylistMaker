package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.converter.TrackEntityConvertor
import com.example.playlistmaker.data.db.database.PlayListMakerRoomDatabase
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavoriteTracksRepositoryImpl(
    private val database: PlayListMakerRoomDatabase
) : FavoriteTracksRepository {

    override suspend fun addTrack(track: Track) {
        withContext(Dispatchers.IO) {
            database.getFavoriteTracksDao().insertTrack(TrackEntityConvertor.convertToEntity(track))
        }
    }

    override suspend fun deleteTrack(track: Track) {
        withContext(Dispatchers.IO) {
            database.getFavoriteTracksDao().deleteTrack(TrackEntityConvertor.convertToEntity(track))
        }
    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        emit(
            database.getFavoriteTracksDao().getAllTracks()
                .map { TrackEntityConvertor.convertToTrack(it) })

    }.flowOn(Dispatchers.IO)

    override fun getAllTrackId(): Flow<List<Long>> = flow {
        emit(database.getFavoriteTracksDao().getAllTrackId())
    }.flowOn(Dispatchers.IO)

    override fun getTrack(id: Long): Flow<Track?> = flow {

        val trackEntity = database.getFavoriteTracksDao().getTrackById(id)

        if (trackEntity == null) {
            emit(null)
        } else {
            emit(TrackEntityConvertor.convertToTrack(trackEntity))
        }

    }.flowOn(Dispatchers.IO)
}