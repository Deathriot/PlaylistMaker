package com.example.playlistmaker.domain.sharing

interface ExternalNavigator {
    fun shareLink(url : String, title : String)

    fun openLink(url : String, title : String)

    fun openEmail(emailData: EmailData, title : String)
}