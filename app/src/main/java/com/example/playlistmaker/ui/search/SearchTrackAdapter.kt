package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ClearHistoryButtonBinding
import com.example.playlistmaker.databinding.TrackBinding
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.util.adapter.TrackAdapter

class SearchTrackAdapter(
    private val onClick: (trackId: Long) -> Unit,
    private val clearBinding: ClearHistoryButtonBinding
) : TrackAdapter(onClick) {

    private var isHistoryShown = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == CLEAR) {
            return ClearButtonViewHolder(clearBinding)
        }

        val binding = TrackBinding.inflate(inflater, parent, false)
        return TrackViewHolder(binding) { position ->
            if (position != RecyclerView.NO_POSITION) {
                tracks.getOrNull(position)?.let { trackInfo ->
                    onClick(trackInfo.id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrackViewHolder) {
            val track = tracks[position]
            holder.bind(track)
        }
    }

    override fun getItemCount(): Int {
        if (isHistoryShown) {
            return tracks.size + 1
        }

        return tracks.size
    }

    override fun setNewTracks(newTracks: List<TrackInfo>) {
        super.setNewTracks(newTracks)
        isHistoryShown = false
    }

    fun setHistoryTracks(newTracks: List<TrackInfo>) {
        super.setNewTracks(newTracks)
        isHistoryShown = true
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == tracks.size && isHistoryShown) CLEAR else TRACK
    }

    companion object {
        private const val CLEAR = 1
        private const val TRACK = 0
    }
}