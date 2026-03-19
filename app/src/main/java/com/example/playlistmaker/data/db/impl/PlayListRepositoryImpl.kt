package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.converter.PlaylistEntityConvertor
import com.example.playlistmaker.data.db.database.PlayListMakerRoomDatabase
import com.example.playlistmaker.data.db.model.PlaylistEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayListRepositoryImpl(
    private val database: PlayListMakerRoomDatabase
) : PlaylistRepository {
    private val gson = Gson()

    override suspend fun savePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            database.getPlaylistDao()
                .insertPlaylist(PlaylistEntityConvertor.convertToEntity(playlist))
        }
    }

    override fun addTrackToPlaylist(trackId: Long, playlistId: Long): Flow<Boolean> = flow {
        val type = object : TypeToken<ArrayList<Long>>() {}.type
        database.getPlaylistDao().getPlaylist(playlistId).collect {
            val trackIdsJson = it.tracksIds
            val trackIds = gson.fromJson<ArrayList<Long>>(trackIdsJson, type)
            if (trackIds.contains(trackId)) {
                emit(false)
            } else {
                trackIds.add(trackId)
                val newTrackIdsJson = gson.toJson(trackIds, type)
                val newTrackCount = it.trackCount + 1
                database.getPlaylistDao()
                    .updatePlaylist(setNewTracks(it, newTrackIdsJson, newTrackCount))
                emit(true)
            }
        }
    }

    override fun getAllPlayLists(): Flow<List<Playlist>> {
        return database.getPlaylistDao().getAllPlaylists().map {
            it.map { playlistEntity ->
                PlaylistEntityConvertor.convertToPlaylist(playlistEntity)
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun setNewTracks(
        playlist: PlaylistEntity,
        newTracks: String,
        trackCount: Int
    ): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            tracksIds = newTracks,
            trackCount = trackCount,
            createdAt = playlist.createdAt
        )
    }
}