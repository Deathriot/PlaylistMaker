package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private val getDarkThemeUseCase = Creator.provideGetDarkThemeUseCase()
    private val setDarkThemeUseCase = Creator.provideSetDarkThemeUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.settings) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initClickListeners()
        setSwitcher()
    }

    private fun setSwitcher() {
        val switch = binding.themeSwitcher

        getDarkThemeUseCase.execute {
            switch.isChecked = it
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            setDarkThemeUseCase.execute(isChecked, {
                AppCompatDelegate.setDefaultNightMode(
                    if (it) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    })
            })
        }
    }

    private fun initClickListeners() {
        binding.settingsBack.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    createShareIntent(),
                    getString(R.string.share_title)
                )
            )
        }

        binding.btnSupport.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    createEmailIntent(),
                    getString(R.string.contact_support)
                )
            )
        }

        binding.btnUserAgreement.setOnClickListener {
            startActivity(createUserAgreementIntent())
        }
    }

    private fun createShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.share_url)
        )

        return shareIntent
    }

    private fun createEmailIntent(): Intent {
        val email = getString(R.string.email_name)
        val subject = getString(R.string.email_title)
        val body = getString(R.string.email_message)

        val emailIntent = Intent(Intent.ACTION_SENDTO);

        emailIntent.data = Uri.parse("mailto:");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        return emailIntent
    }

    private fun createUserAgreementIntent(): Intent {
        val url = getString(R.string.user_agreement_url)
        val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        return agreementIntent
    }
}