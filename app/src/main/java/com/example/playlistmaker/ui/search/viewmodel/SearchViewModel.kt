package com.example.playlistmaker.ui.search.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.GetTracksUseCase
import com.example.playlistmaker.domain.search.HistoryTrackInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.ui.search.mapper.TrackInfoMapper
import com.example.playlistmaker.ui.search.model.State
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.search.viewmodel.model.EditTextState
import com.example.playlistmaker.ui.search.viewmodel.model.SearchConstants

class SearchViewModel(
    private val constants: SearchConstants,
    private val getTracksUseCase : GetTracksUseCase,
    private val historyInteractor : HistoryTrackInteractor
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    private val searchState = MutableLiveData<State>()
    fun observeSearchState(): LiveData<State> = searchState

    private val inputValue = MutableLiveData(EditTextState())
    fun observeEditTextValue(): LiveData<EditTextState> = inputValue

    private val trackDetails = MutableLiveData<TrackDetailsInfo>()
    fun observeOnTrackClick(): LiveData<TrackDetailsInfo> = trackDetails

    private val historyTracks = MutableLiveData(getHistoryTracks())
    fun observeHistory(): LiveData<List<TrackInfo>> = historyTracks

    private var isHistoryShown = true

    fun search() {
        val text = inputValue.value?.text

        if (text.isNullOrEmpty()) {
            return
        }

        handler.removeCallbacks(searchRunnable)
        searchState.postValue(State.Loading)

        getTracksUseCase.execute(title = text,
            consumer = object : Consumer<List<Track>?> {
                override fun consume(data: Result<List<Track>?>) {
                    if (data.isFailure) {
                        searchState.postValue(State.Error(constants.internetError))
                    } else {
                        val tracks = data.getOrNull()

                        if (tracks.isNullOrEmpty()) {
                            searchState.postValue(State.Empty(constants.notFound))
                            return
                        }

                        val tracksInfo = tracks.map { TrackInfoMapper.map(it) }
                        searchState.postValue(State.Content(tracksInfo))
                    }
                }
            })
    }

    fun onTextChanged(s: CharSequence?) {
        val text = s.toString()
        inputValue.postValue(EditTextState(text, inputValue.value!!.isFocused))

        if (text.isEmpty()) {
            isHistoryShown = true
            historyTracks.postValue(getHistoryTracks())
            handler.removeCallbacks(searchRunnable)
        } else {
            isHistoryShown = false
            searchDebounce()
        }
    }

    fun setFocus(isFocused: Boolean) {
        inputValue.postValue(EditTextState(inputValue.value!!.text, isFocused))
    }

    fun onTrackClicked(id: Long) {
        val track = getTracksUseCase.getById(id) ?: historyInteractor.getTrackById(id)
        ?: return

        historyInteractor.addTrack(track)
        val trackDetailsInfo = TrackDetailsInfoMapper.map(track)

        trackDetails.postValue(trackDetailsInfo)

        if (isHistoryShown) {
            historyTracks.postValue(getHistoryTracks())
        }
    }

    private fun getHistoryTracks(): List<TrackInfo> {
        return historyInteractor.getAllTracks().map { TrackInfoMapper.map(it) }
    }

    fun clearHistory() {
        historyInteractor.clearTracks()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onCleared() {
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}