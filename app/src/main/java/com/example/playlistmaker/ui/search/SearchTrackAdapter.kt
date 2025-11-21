package com.example.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.model.TrackInfo

class SearchTrackAdapter(
    private val onClick: (trackId: Long) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks = emptyList<TrackInfo>()

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return TrackViewHolder(view) { position ->
            if (position != RecyclerView.NO_POSITION && clickDebounce()) {
                tracks.getOrNull(position)?.let { trackInfo ->
                    onClick(trackInfo.id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(newTracks : List<TrackInfo>){
        tracks = newTracks
        notifyDataSetChanged()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 800L
    }
}