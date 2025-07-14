package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setIntents()
    }

    private fun setIntents() {
        val btnBack = findViewById<ImageButton>(R.id.settings_back)
        btnBack.setOnClickListener {
            finish()
        }

        val btnShare = findViewById<Button>(R.id.btn_share)
        btnShare.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    createShareIntent(),
                    R.string.share_title.toString()
                )
            )
        }

        val btnEmail = findViewById<Button>(R.id.btn_support)
        btnEmail.setOnClickListener {
            startActivity((createEmailIntent()))
        }

        val btnBrowser = findViewById<Button>(R.id.btn_user_agreement)
        btnBrowser.setOnClickListener {
            startActivity(createUserAgreementIntent())
        }

        val switch = findViewById<SwitchCompat>(R.id.theme_switcher)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun createShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, R.string.share_url.toString()
        )

        return shareIntent
    }

    private fun createEmailIntent(): Intent {
        val email = R.string.email_name
        val subject = R.string.email_title
        val body = R.string.email_message

        val emailIntent = Intent(Intent.ACTION_SENDTO);

        emailIntent.setData(Uri.parse("mailto:$email"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        return emailIntent
    }

    private fun createUserAgreementIntent(): Intent {
        val url = R.string.user_agreement_url.toString()
        val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        return agreementIntent
    }
}