package com.example.playlistmaker.domain.mapper

interface PluralMapper {
    fun getTracksPlural(count: Int) : String
}