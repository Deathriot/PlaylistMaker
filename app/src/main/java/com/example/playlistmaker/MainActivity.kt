package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setDefaultInteractions()
    }

    private fun setDefaultInteractions() {
        val settingsBtn = findViewById<Button>(R.id.btn_settings)
        val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)

        settingsBtn.setOnClickListener {
            startActivity(settingsIntent)
        }

        val mediaBtn = findViewById<Button>(R.id.btn_media_lib)
        val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)

        mediaBtn.setOnClickListener {
            startActivity(mediaIntent)
        }


        val searchBtn = findViewById<Button>(R.id.btn_search)
        val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)

        searchBtn.setOnClickListener {
            startActivity(searchIntent)
        }
    }
}