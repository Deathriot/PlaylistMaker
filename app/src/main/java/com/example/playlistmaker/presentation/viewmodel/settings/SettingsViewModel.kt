package com.example.playlistmaker.presentation.viewmodel.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(
    private val constants: SettingsConstants
) : ViewModel() {
    private val interactor = Creator.provideSettingsInteractor()

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

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as Application)

                return@initializer SettingsViewModel(SettingsConstants(app))
            }
        }
    }
}