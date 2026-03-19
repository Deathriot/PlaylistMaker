package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.converter.FavoriteTrackEntityConvertor
import com.example.playlistmaker.data.db.database.PlayListMakerRoomDatabase
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteTracksRepositoryImpl(
    private val database: PlayListMakerRoomDatabase
) : FavoriteTracksRepository {

    override suspend fun addTrack(track: Track) {
        withContext(Dispatchers.IO) {
            database.getFavoriteTracksDao().insertTrack(FavoriteTrackEntityConvertor.convertToEntity(track))
        }
    }

    override suspend fun deleteTrack(track: Track) {
        withContext(Dispatchers.IO) {
            database.getFavoriteTracksDao().deleteTrack(FavoriteTrackEntityConvertor.convertToEntity(track))
        }
    }

    override fun getAllTracks(): Flow<List<Track>> {
        return database.getFavoriteTracksDao()
            .getAllTracks()
            .map {
                it.map { trackEntity ->
                    FavoriteTrackEntityConvertor.convertToTrack(trackEntity)
                }
            }.flowOn(Dispatchers.IO)
    }


    override fun getAllTrackId(): Flow<List<Long>> {
        return database.getFavoriteTracksDao()
            .getAllTrackId()
            .flowOn(Dispatchers.IO)
    }

    override fun getTrack(id: Long): Flow<Track?> {
        return database.getFavoriteTracksDao()
            .getTrackById(id)
            .map {
                if (it == null) {
                    null
                } else {
                    FavoriteTrackEntityConvertor.convertToTrack(it)
                }
            }.flowOn(Dispatchers.IO)
    }
}