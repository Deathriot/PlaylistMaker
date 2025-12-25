package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.viewmodel.SettingsViewModel
import com.example.playlistmaker.ui.settings.viewmodel.model.SettingsConstants
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsViewModel.setConstants(SettingsConstants(requireContext()))
        initClickListeners()
        setSwitcher()
    }

    private fun setSwitcher() {
        settingsViewModel.observeDarkTheme().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.changeDarkTheme(isChecked)
        }
    }

    private fun initClickListeners() {
        binding.btnShare.setOnClickListener {
            settingsViewModel.share()
        }

        binding.btnSupport.setOnClickListener {
            settingsViewModel.contactSupport()
        }

        binding.btnUserAgreement.setOnClickListener {
            settingsViewModel.openUserAgreement()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
}