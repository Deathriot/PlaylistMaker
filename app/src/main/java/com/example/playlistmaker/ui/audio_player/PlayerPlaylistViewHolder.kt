package com.example.playlistmaker.ui.audio_player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerPlaylistBottomSheetBinding
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.util.dpToPx

class PlayerPlaylistViewHolder(
    private val binding: PlayerPlaylistBottomSheetBinding,
    onClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClick(absoluteAdapterPosition)
        }
    }

    fun bind(playlist: PlaylistDetails) {
        binding.apply {
            playlistBottomSheetTitle.text = playlist.name
            val trackCount = playlist.trackCount

            playlistBottomSheetTrackCount.text =
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
                .into(binding.playlistBottomSheetCover)
        }
    }

    companion object {
        const val CORNER_RADIUS = 2f
    }
}