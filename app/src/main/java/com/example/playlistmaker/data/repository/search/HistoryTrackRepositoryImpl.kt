package com.example.playlistmaker.data.repository.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.search.HistoryTrackRepository
import com.example.playlistmaker.domain.storage.StorageClient

class HistoryTrackRepositoryImpl(
    private val storage : StorageClient<ArrayList<Track>>
) : HistoryTrackRepository {

    private val trackHistory: ArrayList<Track> = storage.getData() ?: ArrayList()

    override fun getAll(): List<Track> {
        return ArrayList(trackHistory)
    }

    override fun add(track: Track) {
        trackHistory.remove(track)

        if(trackHistory.size == MAX_HISTORY_SIZE){
            trackHistory.removeAt(0)
        }

        trackHistory.add(track)
        storage.storeData(trackHistory)
    }

    override fun clear() {
        trackHistory.clear()
        storage.clearData()
    }

    override fun getById(id: Long): Track? {
        return trackHistory.find { it.id == id }
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }
}