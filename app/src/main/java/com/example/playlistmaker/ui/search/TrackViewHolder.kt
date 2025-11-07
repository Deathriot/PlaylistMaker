package com.example.playlistmaker.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.model.TrackInfo
import com.example.playlistmaker.ui.util.dpToPx

class TrackViewHolder(
    itemView: View,
    onClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            onClick(adapterPosition)
        }
    }

    private val image: ImageView = itemView.findViewById(R.id.track_image)
    private val title: TextView = itemView.findViewById(R.id.track_title)
    private val artistName: TextView = itemView.findViewById(R.id.track_artist_name)
    private val time: TextView = itemView.findViewById(R.id.track_time)

    fun bind(track: TrackInfo) {
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, itemView.context)))
            .into(image)

        title.text = track.title
        artistName.text = track.artistName
        time.text = track.time
    }

    companion object {
        private const val CORNER_RADIUS = 2f
    }
}
