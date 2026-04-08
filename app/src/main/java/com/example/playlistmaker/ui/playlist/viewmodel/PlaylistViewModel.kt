package com.example.playlistmaker.ui.playlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.DeletePlaylistTrackUseCase
import com.example.playlistmaker.domain.playlist.DeletePlaylistUseCase
import com.example.playlistmaker.domain.playlist.GetAllPlaylistTracksUseCase
import com.example.playlistmaker.domain.playlist.GetPlaylistTrackUseCase
import com.example.playlistmaker.domain.playlist.GetPlaylistUseCase
import com.example.playlistmaker.domain.playlist.navigation.SharePlaylistUseCase
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.model.SingleLiveEvent
import com.example.playlistmaker.ui.playlist.mapper.PlaylistDetailsInfoMapper
import com.example.playlistmaker.ui.playlist.mapper.ShareMapper
import com.example.playlistmaker.ui.playlist.model.PlaylistDetailsInfo
import com.example.playlistmaker.ui.search.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.ui.search.mapper.TrackInfoMapper
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.search.model.TrackInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val getAllPlaylistTracksUseCase: GetAllPlaylistTracksUseCase,
    private val getPlaylistTrackUseCase: GetPlaylistTrackUseCase,
    private val deletePlaylistTrackUseCase: DeletePlaylistTrackUseCase,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel() {
    private val currentPlaylist = MutableLiveData<PlaylistDetailsInfo>()
    fun observeCurrentPlaylist(): LiveData<PlaylistDetailsInfo> = currentPlaylist

    private val playlistTracks = MutableLiveData<List<TrackInfo>>()
    fun observePlaylistTracks(): LiveData<List<TrackInfo>> = playlistTracks

    private val clickedTrack = SingleLiveEvent<TrackDetailsInfo>()
    fun observeClickedTrack(): LiveData<TrackDetailsInfo> = clickedTrack

    fun onTrackClick(trackId: Long) {
        viewModelScope.launch {
            val track = getPlaylistTrackUseCase.execute(trackId).first()
            if (track != null) {
                val trackDetailsInfo = TrackDetailsInfoMapper.mapToTrackDetailsInfo(track)
                clickedTrack.postValue(trackDetailsInfo)
            }
        }
    }

    fun sharePlaylist() {
        val playlist = ShareMapper.mapToPlaylistForShare(currentPlaylist.value!!)
        val tracks = playlistTracks.value!!.map { ShareMapper.mapToTrackForShare(it) }
        sharePlaylistUseCase.execute(playlist, tracks)
    }

    fun deleteTrack(trackId: Long) {
        viewModelScope.launch {
            deletePlaylistTrackUseCase.execute(trackId, currentPlaylist.value!!.id)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            val playlist = PlaylistDetailsInfoMapper.mapToPlaylist(currentPlaylist.value!!)
            deletePlaylistUseCase.execute(playlist)
        }
    }

    fun loadPlaylist(playlistId: Long) {
        updatePlaylist(playlistId)
    }

    private fun updatePlaylist(playlistId: Long) {
        viewModelScope.launch {
            getPlaylistUseCase.execute(playlistId)
                .takeWhile { playlist ->
                    playlist != null
                }
                .collect {
                    val tracks = getAllPlaylistTracksUseCase.execute(it!!.tracksIds).first()

                    val playlistDetailsInfo = PlaylistDetailsInfoMapper.mapToDetailsInfo(
                        it,
                        calculateTotalDurationMillis(tracks)
                    )

                    currentPlaylist.postValue(playlistDetailsInfo)

                    val tracksInfo = tracks.map { track ->
                        TrackInfoMapper.mapToInfo(track)
                    }

                    playlistTracks.postValue(tracksInfo)
                }
        }
    }


    private fun calculateTotalDurationMillis(tracks: List<Track>): Long {
        return tracks.sumOf { it.timeMillis }
    }
}