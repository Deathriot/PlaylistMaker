package com.example.playlistmaker.utils.services.media_player_service

import com.example.playlistmaker.ui.audio_player.model.PlayerState
import com.example.playlistmaker.ui.audio_player.model.TotalPlayerState
import kotlinx.coroutines.flow.StateFlow

interface MediaPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun changeState()
}