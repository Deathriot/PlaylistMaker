package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.HistoryTrackRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class HistoryTrackRepositoryImpl(
    private val prefs: SharedPreferences
) : HistoryTrackRepository {

    private val gson = GsonBuilder().create()
    private val trackHistory: ArrayList<Track>

    init {
        trackHistory = getTracksFromPrefs()
    }

    override fun getAll(): List<Track> {
        return ArrayList(trackHistory)
    }

    override fun add(track: Track) {
        trackHistory.remove(track)

        if(trackHistory.size == MAX_HISTORY_SIZE){
            trackHistory.removeAt(0)
        }

        trackHistory.add(track)
        val newTracksJson = gson.toJson(trackHistory)
        prefs.edit().putString(SEARCH_HISTORY_KEY, newTracksJson).apply()
    }

    override fun clear() {
        trackHistory.clear()
        prefs.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    override fun getById(id: Long): Track? {
        return trackHistory.find { it.id == id }
    }

    private fun getTracksFromPrefs() : ArrayList<Track>{
        val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
        val tracksJson = prefs.getString(SEARCH_HISTORY_KEY, "[]")
        return gson.fromJson(tracksJson, typeToken)
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"

        private const val MAX_HISTORY_SIZE = 10
    }
}