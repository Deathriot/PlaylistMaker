package com.example.playlistmaker.ui.media

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistBinding
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.util.dpToPx

class PlaylistViewHolder(
    private val binding: PlaylistBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(playlist: PlaylistDetails) {
        binding.apply {
            playlistName.text = playlist.name
            val trackCount = playlist.trackCount

            playlistTrackCount.text =
                itemView.resources.getQuantityString(
                    R.plurals.plurals_playlists_tracks,
                    trackCount,
                    trackCount
                )

            Glide.with(binding.root)
                .load(playlist.coverUri)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(dpToPx(CORNER_RADIUS, binding.root.context))
                    )
                )
                .placeholder(R.drawable.ic_placeholder_45)
                .into(binding.playlistCover)
        }
    }

    companion object {
        const val CORNER_RADIUS = 8f
    }
}