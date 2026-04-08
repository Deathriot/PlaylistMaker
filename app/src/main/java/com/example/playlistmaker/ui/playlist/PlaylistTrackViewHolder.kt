package com.example.playlistmaker.ui.playlist

import com.example.playlistmaker.databinding.TrackBinding
import com.example.playlistmaker.ui.search.TrackViewHolder

class PlaylistTrackViewHolder(
    binding: TrackBinding,
    onClick: (position: Int) -> Unit,
    private val onLongClick: (position: Int) -> Unit
) : TrackViewHolder(binding, onClick) {
    init {
        binding.root.setOnLongClickListener {
            onLongClick(absoluteAdapterPosition)
            true
        }
    }
}