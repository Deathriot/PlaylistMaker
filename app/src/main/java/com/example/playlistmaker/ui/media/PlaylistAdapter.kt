package com.example.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.playlistmaker.databinding.PlaylistBinding
import com.example.playlistmaker.ui.media.model.PlaylistDetails

class PlaylistAdapter : Adapter<PlaylistViewHolder>() {
    private var playlists = ArrayList<PlaylistDetails>()

    fun setContent(playlists: List<PlaylistDetails>) {
        this.playlists = ArrayList(playlists)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistBinding.inflate(inflater, parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}