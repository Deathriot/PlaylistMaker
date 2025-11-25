package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.domain.sharing.model.EmailData

interface SettingsInteractor {
    fun openUserAgreement(url: String, title : String)

    fun contactSupport(emailData: EmailData, title : String)

    fun shareApp(url: String, title : String)

    fun setDarkTheme(isDark : Boolean)

    fun getDarkTheme(consumer: (Boolean) -> Unit)
}