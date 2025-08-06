package com.example.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track

class SearchTrackAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val CORNER_RADIUS = 2f

    private val image: ImageView = itemView.findViewById(R.id.track_image)
    private val title: TextView = itemView.findViewById(R.id.track_title)
    private val artistName: TextView = itemView.findViewById(R.id.track_artist_name)
    private val time: TextView = itemView.findViewById(R.id.track_time)

    fun bind(track: Track) {
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


    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}