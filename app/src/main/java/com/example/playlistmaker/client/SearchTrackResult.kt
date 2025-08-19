package com.example.playlistmaker.client

import com.example.playlistmaker.model.Track

data class SearchTrackResult(var tracks : List<Track>?,var status : SearchStatus)