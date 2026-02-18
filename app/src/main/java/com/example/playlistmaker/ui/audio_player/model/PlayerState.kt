package com.example.playlistmaker.ui.audio_player.model

import com.example.playlistmaker.domain.player.model.MediaPlayerState

data class PlayerState(
    var mediaState: MediaPlayerState = MediaPlayerState.STATE_DEFAULT,

    var timer: String,

    var isLiked: Boolean
)
