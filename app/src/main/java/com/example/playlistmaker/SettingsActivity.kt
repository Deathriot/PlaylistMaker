package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val mainIntent = Intent(this@SettingsActivity, MainActivity::class.java)

        val btnBack = findViewById<ImageButton>(R.id.settings_back)

        btnBack.setOnClickListener{
            startActivity(mainIntent)
        }
    }
}