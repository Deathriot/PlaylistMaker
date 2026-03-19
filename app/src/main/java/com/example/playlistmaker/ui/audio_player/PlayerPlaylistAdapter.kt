package com.example.playlistmaker.ui.audio_player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.playlistmaker.databinding.PlayerPlaylistBottomSheetBinding
import com.example.playlistmaker.ui.media.model.PlaylistDetails

class PlayerPlaylistAdapter(
    private val onClick: (playlistId: Long, playlistName: String) -> Unit
) : Adapter<PlayerPlaylistViewHolder>() {
    private var playlists = ArrayList<PlaylistDetails>()

    fun setContent(playlists: List<PlaylistDetails>) {
        this.playlists = ArrayList(playlists)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlayerPlaylistBottomSheetBinding.inflate(inflater, parent, false)
        return PlayerPlaylistViewHolder(binding) { position ->
            if (position != RecyclerView.NO_POSITION) {
                playlists.getOrNull(position)?.let { playlistDetails ->
                    onClick(playlistDetails.id, playlistDetails.name)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PlayerPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}