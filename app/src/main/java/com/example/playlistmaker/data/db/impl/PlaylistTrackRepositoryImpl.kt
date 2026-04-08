package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.converter.PlaylistTrackEntityConvertor
import com.example.playlistmaker.data.db.database.PlayListMakerRoomDatabase
import com.example.playlistmaker.domain.db.repository.PlaylistTrackRepository
import com.example.playlistmaker.domain.search.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PlaylistTrackRepositoryImpl(
    private val database: PlayListMakerRoomDatabase
) : PlaylistTrackRepository {
    private val gson = Gson()
    private val type = object : TypeToken<ArrayList<Long>>() {}.type
    override fun getTracksByIds(jsonIds: String): Flow<List<Track>> {

        val tracksId = gson.fromJson<ArrayList<Long>>(jsonIds, type)

        return database.getPlaylistTrackDao().getTracksByIds(tracksId).map {
            it.mapNotNull { playlistTrackEntity ->
                PlaylistTrackEntityConvertor.convertToTrack(playlistTrackEntity)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getTrackById(trackId: Long): Flow<Track?> {
        return database.getPlaylistTrackDao().getTrackById(trackId).map {
            PlaylistTrackEntityConvertor.convertToTrack(it)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteTrackById(trackId: Long) {
        val playlists = database.getPlaylistDao().getAllPlaylists().first()

        playlists.forEach {
            if (it.tracksIds.contains(trackId.toString())) {
                return
            }
        }

        database.getPlaylistTrackDao().deleteTrack(trackId)
    }

    override suspend fun deleteAllTracksFromPlaylist(playlistId: Long) {
        val fromPlaylist = database.getPlaylistDao().getPlaylist(playlistId).first() ?: return
        val playlists = database.getPlaylistDao().getAllPlaylists().first()

        val tracksIds = gson.fromJson<ArrayList<Long>>(fromPlaylist.tracksIds, type)
        val deletedTracks = arrayListOf<Long>()

        tracksIds.forEach { trackId ->
            var shouldDelete = true
            for (playlist in playlists) {
                if (playlist.id == fromPlaylist.id) {
                    continue
                }
                if (playlist.tracksIds.contains(trackId.toString())) {
                    shouldDelete = false
                    break
                }
            }
            if (shouldDelete) {
                deletedTracks.add(trackId)
            }
        }

        database.getPlaylistTrackDao().deleteTracks(deletedTracks)
    }

    override suspend fun insertTrack(track: Track) {
        val playListTrack = PlaylistTrackEntityConvertor.convertToEntity(track)
        database.getPlaylistTrackDao().insertTrack(playListTrack)
    }
}