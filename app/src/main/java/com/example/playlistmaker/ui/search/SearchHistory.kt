package com.example.playlistmaker.ui.search

import android.view.View
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.mapper.TrackInfoMapper

class SearchHistory(private val binding: ActivitySearchBinding) {
    private val historyTrackInteractor = Creator.provideHistoryTrackInteractor()

    init {
        setClearHistoryBtnAction()
    }

    fun addTrack(newTrack: Track) {
        historyTrackInteractor.addTrack(newTrack)
    }

    fun getTrackFromHistory(id : Long) : Track?{
        return historyTrackInteractor.getTrackById(id)
    }

    fun showHistory() {
        val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter

        if (isHistoryEmpty()) {
            adapter.setTracks(emptyList())
            return
        }

        binding.searchHistoryTitle.visibility = View.VISIBLE
        binding.searchClearHistory.visibility = View.VISIBLE
        binding.searchRecycleView.visibility = View.VISIBLE
        val tracksInfo = historyTrackInteractor.getAllTracks().map { TrackInfoMapper.map(it) }
        adapter.setTracks(tracksInfo)
    }

    fun hideHistory() {
        binding.searchHistoryTitle.visibility = View.GONE
        binding.searchClearHistory.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE
    }

    private fun setClearHistoryBtnAction() {
        binding.searchClearHistory.setOnClickListener {
            clearSearchHistory()
            hideHistory()
        }
    }

    private fun clearSearchHistory() {
        val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter
        historyTrackInteractor.clearTracks()
        adapter.setTracks(emptyList())
    }

    fun isHistoryEmpty(): Boolean {
        return historyTrackInteractor.getAllTracks().isEmpty()
    }
}