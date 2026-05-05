package com.example.playlistmaker.utils.services.media_player_service.converter

import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.utils.services.media_player_service.model.ServiceTrackData

object ServiceTrackDataConverter {
    fun convertToServiceTrackData(trackDetailsInfo: TrackDetailsInfo): ServiceTrackData {
        return ServiceTrackData(
            info = "${trackDetailsInfo.artistName} - ${trackDetailsInfo.title}",
            songUrl = trackDetailsInfo.musicUrl
        )
    }
}