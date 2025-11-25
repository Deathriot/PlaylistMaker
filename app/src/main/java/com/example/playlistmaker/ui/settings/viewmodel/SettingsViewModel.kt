package com.example.playlistmaker.ui.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.ui.settings.viewmodel.model.SettingsConstants

class SettingsViewModel(
    private val constants: SettingsConstants,
    private val interactor : SettingsInteractor

) : ViewModel() {

    private val isDarkTheme = MutableLiveData<Boolean>()

    fun observeDarkTheme(): LiveData<Boolean> = isDarkTheme

    init {
        getDarkTheme()
    }

    fun changeDarkTheme(isDark: Boolean) {
        interactor.setDarkTheme(isDark)
        isDarkTheme.value = isDark
    }

    fun openUserAgreement() {
        interactor.openUserAgreement(constants.userAgreementUrl, "")
    }

    fun share() {
        interactor.shareApp(constants.appUrl, constants.shareTitle)
    }

    fun contactSupport() {
        interactor.contactSupport(constants.emailData, constants.contactSupportTitle)
    }

    private fun getDarkTheme() {
        interactor.getDarkTheme {
            isDarkTheme.postValue(it)
        }
    }
}