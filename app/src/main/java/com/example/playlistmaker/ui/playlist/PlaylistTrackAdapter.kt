package com.example.playlistmaker.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackBinding
import com.example.playlistmaker.ui.util.adapter.TrackAdapter

class PlaylistTrackAdapter(
    private val onClick: (trackId: Long) -> Unit,
    private val onLongClick: (trackId: Long) -> Unit
) : TrackAdapter(onClick) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackBinding.inflate(inflater, parent, false)
        return PlaylistTrackViewHolder(
            binding = binding,
            onClick = { position ->
                if (position != RecyclerView.NO_POSITION) {
                    tracks.getOrNull(position)?.let { trackInfo ->
                        onClick(trackInfo.id)
                    }
                }
            },
            onLongClick = { position ->
                if (position != RecyclerView.NO_POSITION) {
                    tracks.getOrNull(position)?.let { trackInfo ->
                        onLongClick(trackInfo.id)
                    }
                }
            })
    }
}