package com.example.playlistmaker.presentation.viewmodel.settings

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.EmailData

class SettingsConstants(
    app: Context
) {
    private val email = app.getString(R.string.email_name)
    private val subject = app.getString(R.string.email_title)
    private val body = app.getString(R.string.email_message)

    val emailData = EmailData(email, subject, body)
    val shareTitle = app.getString(R.string.share_title)
    val contactSupportTitle = app.getString(R.string.contact_support)
    val userAgreementUrl = app.getString(R.string.user_agreement_url)
    val appUrl = app.getString(R.string.share_url)
}



