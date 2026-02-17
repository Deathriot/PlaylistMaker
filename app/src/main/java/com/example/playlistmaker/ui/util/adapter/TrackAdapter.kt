package com.example.playlistmaker.ui.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.playlistmaker.databinding.TrackBinding
import com.example.playlistmaker.ui.search.TrackViewHolder
import com.example.playlistmaker.ui.search.model.TrackInfo

open class TrackAdapter(private val onClick: (trackId: Long) -> Unit) :
    Adapter<RecyclerView.ViewHolder>() {

    protected var tracks: List<TrackInfo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
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
        } else {
            throw IllegalArgumentException("В адаптер закрался не трек")
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    open fun setNewTracks(newTracks: List<TrackInfo>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return tracks.isEmpty()
    }
}