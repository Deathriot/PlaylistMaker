package com.example.playlistmaker.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.GetTracksUseCase
import com.example.playlistmaker.domain.search.HistoryTrackInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.model.SingleLiveEvent
import com.example.playlistmaker.ui.search.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.ui.search.mapper.TrackInfoMapper
import com.example.playlistmaker.ui.search.model.State
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.search.viewmodel.model.EditTextState
import com.example.playlistmaker.ui.search.viewmodel.model.SearchConstants
import com.example.playlistmaker.ui.util.Debouncer
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getTracksUseCase: GetTracksUseCase,
    private val historyInteractor: HistoryTrackInteractor
) : ViewModel() {
    private val searchDebounce =
        Debouncer.debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { text ->
            search(text)
        }

    private val searchState = SingleLiveEvent<State>()
    fun observeSearchState(): LiveData<State> = searchState

    private val inputValue = MutableLiveData<EditTextState>()
    fun observeEditTextValue(): LiveData<EditTextState> = inputValue

    private val trackDetails = SingleLiveEvent<TrackDetailsInfo>()
    fun observeOnTrackClick(): LiveData<TrackDetailsInfo> = trackDetails

    private val historyTracks = MutableLiveData(getHistoryTracks())
    fun observeHistory(): LiveData<List<TrackInfo>> = historyTracks

    private lateinit var constants: SearchConstants

    init {
        inputValue.postValue(EditTextState())
    }

    fun search(text: String?) {
        if (text.isNullOrEmpty()) {
            return
        }

        searchState.postValue(State.Loading)

        viewModelScope.launch {
            getTracksUseCase.execute(text)
                .collect {
                    processResult(it)
                }
        }
    }

    fun onTextChanged(s: CharSequence?) {
        val text = s.toString()
        inputValue.postValue(EditTextState(text, inputValue.value!!.isFocused))

        if (text.isEmpty()) {
            Debouncer.cancel()
            historyTracks.postValue(getHistoryTracks())
        } else {
            if (text == inputValue.value?.text) {
                search(text)
            } else {
                searchDebounce(text)
            }
        }
    }

    fun setFocus(isFocused: Boolean) {
        inputValue.postValue(EditTextState(inputValue.value!!.text, isFocused))
    }

    fun onTrackClicked(id: Long) {
        val track = getTracksUseCase.getById(id) ?: historyInteractor.getTrackById(id)
        ?: return

        Debouncer.cancel()
        historyInteractor.addTrack(track)
        val trackDetailsInfo = TrackDetailsInfoMapper.map(track)

        trackDetails.postValue(trackDetailsInfo)
    }

    fun performSearch() {
        search(inputValue.value?.text)
    }

    fun setConstants(constants: SearchConstants) {
        this.constants = constants
    }

    private fun processResult(result: Result<List<Track>?>) {
        if (result.isFailure) {
            searchState.postValue(State.Error(constants.internetError))
        } else {
            val tracks = result.getOrNull()

            if (tracks.isNullOrEmpty()) {
                searchState.postValue(State.Empty(constants.notFound))
                return
            }

            val tracksInfo = tracks.map { TrackInfoMapper.map(it) }
            searchState.postValue(State.Content(tracksInfo))
        }
    }

    private fun getHistoryTracks(): List<TrackInfo> {
        return historyInteractor.getAllTracks().map { TrackInfoMapper.map(it) }
    }

    fun clearHistory() {
        historyInteractor.clearTracks()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}