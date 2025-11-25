package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.viewmodel.settings.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel : SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.settings) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory = SettingsViewModel.getFactory()
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
        initClickListeners()
        setSwitcher()
    }

    private fun setSwitcher() {
        viewModel.observeDarkTheme().observe(this){
            binding.themeSwitcher.isChecked = it
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeDarkTheme(isChecked)
        }
    }

    private fun initClickListeners() {
        binding.settingsBack.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            viewModel.share()
        }

        binding.btnSupport.setOnClickListener {
            viewModel.contactSupport()
        }

        binding.btnUserAgreement.setOnClickListener {
            viewModel.openUserAgreement()
        }
    }
}