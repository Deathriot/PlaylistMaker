package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.exception.TrackNotFoundException
import com.example.playlistmaker.ui.model.SingleLiveEvent
import com.example.playlistmaker.ui.search.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.ui.search.mapper.TrackInfoMapper
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.util.Debouncer
import com.example.playlistmaker.ui.util.State
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    init {
        loadTracks()
    }

    private val state = MutableLiveData<State>()
    fun observeState(): LiveData<State> = state

    private val trackDetails = SingleLiveEvent<TrackDetailsInfo>()
    fun observeTrackDetails(): LiveData<TrackDetailsInfo> = trackDetails

    fun onTrackClicked(id: Long) {
        viewModelScope.launch {
            favoriteTracksInteractor.getTrack(id).collect {
                if (it == null) {
                    throw TrackNotFoundException()
                } else {
                    val trackDetailsInfo =
                        TrackDetailsInfoMapper.mapFromFavoriteTrackToTrackDetailsInfo(it)
                    trackDetails.postValue(trackDetailsInfo)
                }
            }
            Debouncer.cancel()
        }
    }

    fun loadTracks() {
        viewModelScope.launch {
            favoriteTracksInteractor.getAllTracks().collect { tracks ->
                if (tracks.isEmpty()) {
                    state.postValue(State.Empty("Избранных треков нет"))
                } else {
                    val tracksInfo = tracks.map { TrackInfoMapper.map(it) }
                    state.postValue(State.Content(tracksInfo))
                }
            }
        }
    }
}