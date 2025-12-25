package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.ui.media.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private var tabMediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mediaViewPager.adapter = MediaViewPagerAdapter(this)
        initMediator()
    }

    private fun initMediator() {
        tabMediator =
            TabLayoutMediator(binding.mediaTabLayout, binding.mediaViewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.media_favorite_tracks)
                    1 -> tab.text = getString(R.string.media_playlists)
                }
            }

        tabMediator?.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
        tabMediator = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }
}