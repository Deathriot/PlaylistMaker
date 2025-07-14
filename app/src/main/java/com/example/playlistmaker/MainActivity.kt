package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setDefaultInteractions()
    }

    private fun setDefaultInteractions() {
        val settingsBtn = findViewById<Button>(R.id.btn_settings)

        settingsBtn.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        val mediaBtn = findViewById<Button>(R.id.btn_media_lib)

        mediaBtn.setOnClickListener {
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        val searchBtn = findViewById<Button>(R.id.btn_search)

        searchBtn.setOnClickListener {
            val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(searchIntent)
        }
    }
}