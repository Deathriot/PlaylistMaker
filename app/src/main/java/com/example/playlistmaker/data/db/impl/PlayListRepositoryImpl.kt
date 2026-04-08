package com.example.playlistmaker.data.db.impl

import android.util.Log
import com.example.playlistmaker.data.db.converter.PlaylistEntityConvertor
import com.example.playlistmaker.data.db.database.PlayListMakerRoomDatabase
import com.example.playlistmaker.data.db.model.PlaylistEntity
import com.example.playlistmaker.domain.db.repository.PlaylistRepository
import com.example.playlistmaker.domain.new_playlist.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val database: PlayListMakerRoomDatabase
) : PlaylistRepository {
    private val gson = Gson()
    private val type = object : TypeToken<ArrayList<Long>>() {}.type


    override suspend fun savePlaylist(playlist: Playlist) {
        val dao = database.getPlaylistDao()
        val playlistEntity = PlaylistEntityConvertor.convertToEntity(playlist)
        if(playlist.id == 0L){
            dao.insertPlaylist(playlistEntity)
        } else {
            dao.updatePlaylist(playlistEntity)
        }
    }

    override fun addTrackToPlaylist(trackId: Long, playlistId: Long): Flow<Boolean> = flow {
        database.getPlaylistDao().getPlaylist(playlistId).collect {
            if (it == null) {
                emit(false)
                return@collect
            }

            val trackIdsJson = it.tracksIds
            val trackIds = gson.fromJson<ArrayList<Long>>(trackIdsJson, type)

            if (trackIds.contains(trackId)) {
                emit(false)
            } else {
                val updatedPlaylist = addTrack(it, trackIds, trackId)
                database.getPlaylistDao()
                    .updatePlaylist(updatedPlaylist)
                emit(true)
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deletePlaylist(playlistId: Long) {
        database.getPlaylistDao().deletePlaylistById(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        val playlist = database.getPlaylistDao().getPlaylist(playlistId).first() ?: return

        val trackIdsJson = playlist.tracksIds
        val trackIds = gson.fromJson<ArrayList<Long>>(trackIdsJson, type)

        val updatedPlaylist = removeTrack(playlist, trackIds, trackId)

        database.getPlaylistDao()
            .updatePlaylist(updatedPlaylist)
        Log.i("PlayListRepository", "deleted track $trackId from $playlistId playlist")
    }

    override fun getAllPlayLists(): Flow<List<Playlist>> {
        return database.getPlaylistDao().getAllPlaylists().map {
            it.map { playlistEntity ->
                PlaylistEntityConvertor.convertToPlaylist(playlistEntity)!!
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist?> {
        return database.getPlaylistDao().getPlaylist(playlistId).map {
            PlaylistEntityConvertor.convertToPlaylist(it)
        }.flowOn(Dispatchers.IO)
    }

    private fun removeTrack(
        playlist: PlaylistEntity,
        trackIds: ArrayList<Long>,
        trackId: Long
    ): PlaylistEntity {
        trackIds.remove(trackId)
        val newTrackCount = trackIds.size
        val newTrackIdsJson = gson.toJson(trackIds, type)

        return playlist.copy(tracksIds = newTrackIdsJson, trackCount = newTrackCount)
    }

    private fun addTrack(
        playlist: PlaylistEntity,
        trackIds: ArrayList<Long>,
        trackId: Long
    ): PlaylistEntity {
        trackIds.add(trackId)
        val newTrackCount = trackIds.size
        val newTrackIdsJson = gson.toJson(trackIds, type)

        return playlist.copy(tracksIds = newTrackIdsJson, trackCount = newTrackCount)
    }
}