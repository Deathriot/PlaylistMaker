package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.media.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.media.PlaylistAdapter
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.media.item_decoration.PlaylistItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private lateinit var adapter: PlaylistAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initAdapter()
        setObservers()
    }

    private fun initClickListeners() {
        binding.playlistsAddNewPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaFragment_to_newPlaylistFragment
            )
        }
    }

    private fun setObservers() {
        playlistsViewModel.observePlaylists().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(playlists: List<PlaylistDetails>) {
        if (playlists.isEmpty()) {
            showPlaceholder()
        } else {
            showContent(playlists)
        }
    }

    private fun showContent(playlists: List<PlaylistDetails>) {
        binding.apply {
            playlistsRecycleView.isVisible = true
            playlistsPlaceholder.isVisible = false
        }

        adapter.setContent(playlists)
    }

    private fun showPlaceholder() {
        binding.apply {
            playlistsRecycleView.isVisible = false
            playlistsPlaceholder.isVisible = true
        }
    }

    private fun initAdapter() {
        val spacing = resources.getDimensionPixelSize(R.dimen.normal_padding)
        val decoration = PlaylistItemDecoration(spacing)
        adapter = PlaylistAdapter()
        binding.playlistsRecycleView.apply {
            this.layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(decoration)
            this.adapter = this@PlaylistsFragment.adapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.loadPlaylists()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}