package com.example.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import com.google.gson.GsonBuilder

class SearchTrackAdapter(
    val prefs: SharedPreferences
) : RecyclerView.Adapter<TrackViewHolder>() {
    private val gson = GsonBuilder().create()
    var tracks = emptyList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)

        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        setClickListener(holder.itemView, track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private fun setClickListener(itemView: View, track: Track) {
        itemView.setOnClickListener {
            val trackJson = gson.toJson(track)
            prefs.edit().putString(App.SEARCH_NEW_TRACK_KEY, trackJson).apply()
        }
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