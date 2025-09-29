package com.example.playlistmaker.util

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.view.View
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class SearchHistory(val binding: ActivitySearchBinding, val prefs: SharedPreferences) {
    private val listener: OnSharedPreferenceChangeListener
    private val gson = GsonBuilder().create()

    // Вводим переменную, чтобы при показе одной и той же истории треков не приходилось загружать список из префа
    private val trackHistory: ArrayList<Track>
    private val MAX_HISTORY_SIZE = 10
    init {
        val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
        val tracksJson = prefs.getString(App.SEARCH_HISTORY_KEY, "[]")
        trackHistory = gson.fromJson(tracksJson, typeToken)

        listener = OnSharedPreferenceChangeListener { prefs, key ->
            if (key == App.SEARCH_NEW_TRACK_KEY) {
                val trackJson = prefs.getString(App.SEARCH_NEW_TRACK_KEY, null)
                    ?: throw RuntimeException("Добавленный трек null, такого не может быть, проверяй код")

                val track = gson.fromJson(trackJson, Track::class.java)
                addTrack(track)
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
        setClearHistoryBtnAction()
    }

    private fun addTrack(newTrack: Track) {

        trackHistory.remove(newTrack)

        if (trackHistory.size == MAX_HISTORY_SIZE) {
            trackHistory.removeAt(0)
        }

        trackHistory.add(newTrack)

        val newTracksJson = gson.toJson(trackHistory)
        prefs.edit().putString(App.SEARCH_HISTORY_KEY, newTracksJson).apply()
    }

    private fun clearSearchHistory() {
        prefs.edit().remove(App.SEARCH_HISTORY_KEY).apply()
        val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter
        trackHistory.clear()
        adapter.tracks = emptyList()
        adapter.notifyDataSetChanged()
    }

    fun showHistory() {
        if (trackHistory.isEmpty()) {
            return
        }

        binding.searchHistoryTitle.visibility = View.VISIBLE
        binding.searchClearHistory.visibility = View.VISIBLE
        binding.searchRecycleView.visibility = View.VISIBLE
        val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter
        adapter.tracks = trackHistory.reversed()
        adapter.notifyDataSetChanged()
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

    fun isHistoryEmpty() : Boolean{
        return trackHistory.isEmpty()
    }
}