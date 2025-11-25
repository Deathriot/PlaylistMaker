package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.GetDarkThemeUseCase
import com.example.playlistmaker.domain.settings.SetDarkThemeUseCase
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.domain.sharing.ExternalNavigator

class SettingsInteractorImpl(
    private val navigator: ExternalNavigator,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val getDarkThemeUseCase: GetDarkThemeUseCase
) : SettingsInteractor {
    override fun openUserAgreement(url: String, title : String) {
        navigator.openLink(url, title)
    }

    override fun contactSupport(emailData: EmailData, title : String) {
        navigator.openEmail(emailData, title)
    }

    override fun shareApp(url: String, title : String) {
        navigator.shareLink(url, title)
    }

    override fun setDarkTheme(isDark: Boolean) {
        setDarkThemeUseCase.execute(isDark)
    }

    override fun getDarkTheme(consumer: (Boolean) -> Unit) {
        getDarkThemeUseCase.execute(consumer)
    }
}