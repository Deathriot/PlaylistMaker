package com.example.playlistmaker.data.mapper.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.mapper.PluralMapper

class PluralMapperImpl(
    private val context: Context
) : PluralMapper {
    override fun getTracksPlural(count: Int): String {
        return context.resources.getQuantityString(R.plurals.plurals_playlists_tracks, count, count)
    }
}