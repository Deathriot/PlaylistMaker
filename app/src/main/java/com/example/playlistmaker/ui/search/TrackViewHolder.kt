package com.example.playlistmaker.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackBinding
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.util.dpToPx

open class TrackViewHolder(
    private val binding: TrackBinding,
    onClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClick(absoluteAdapterPosition)
        }
    }

    fun bind(track: TrackInfo) {
        with(binding) {
            Glide.with(root)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(CORNER_RADIUS, root.context)))
                .into(trackImage)

            trackTitle.text = track.title
            trackArtistName.text = track.artistName
            trackTime.text = track.time
        }
    }

    companion object {
        private const val CORNER_RADIUS = 2f
    }
}
