package com.example.playlistmaker.data.repository.settings

import com.example.playlistmaker.domain.repository.settings.DarkThemeRepository
import com.example.playlistmaker.domain.storage.StorageClient

class DarkThemeRepositoryImpl(
    private val storage: StorageClient<Boolean>
) : DarkThemeRepository {
    override fun isDark(): Boolean {
        return storage.getData() ?: false
    }

    override fun saveTheme(isDark: Boolean) {
        storage.storeData(isDark)
    }
}