package com.example.playlistmaker.ui.new_playlist.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.new_playlist.model.PlaylistInfo
import com.example.playlistmaker.ui.new_playlist.viewmodel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : NewPlaylistFragment() {
    override val newPlaylistViewModel: EditPlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        val playlistId = requireArguments().getLong(ARGS_PLAYLIST_ID)
        newPlaylistViewModel.loadPlaylist(playlistId)
    }

    override fun setObservers() {
        newPlaylistViewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) {
            render(it)
        }

        newPlaylistViewModel.observeCurrentTrackCover().observe(viewLifecycleOwner) {
            setTrackCover(it)
        }

        newPlaylistViewModel.observePlaylistSaved().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    private fun setUI() {
        binding.newPlaylistTitle.text = getString(R.string.edit_playlist_title)
        binding.newPlaylistCreateBtn.text = getString(R.string.edit_playlist_save_btn_text)
    }

    private fun render(playlist: PlaylistInfo) {
        binding.newPlaylistName.setText(playlist.name)
        binding.newPlaylistDescription.setText(playlist.description)
        setTrackCover(playlist.coverUri)
    }

    override fun setOnBackPressed() {}

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long) = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }
}