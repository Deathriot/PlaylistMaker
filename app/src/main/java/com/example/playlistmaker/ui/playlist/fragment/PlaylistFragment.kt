package com.example.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.audio_player.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.new_playlist.fragment.EditPlaylistFragment
import com.example.playlistmaker.ui.playlist.PlaylistTrackAdapter
import com.example.playlistmaker.ui.playlist.model.PlaylistDetailsInfo
import com.example.playlistmaker.ui.playlist.viewmodel.PlaylistViewModel
import com.example.playlistmaker.ui.util.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var adapter: PlaylistTrackAdapter
    private lateinit var tracksBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var tracksBottomSheetCallback: BottomSheetCallback

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetCallback: BottomSheetCallback

    private lateinit var playlistName: String
    private var playlistId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = requireArguments().getLong(ARG_PLAYLIST_ID)
        playlistViewModel.loadPlaylist(playlistId)

        initAdapter()
        initClickListeners()
        setObservers()
        setBottomSheet()
        setOnBackDispatcher()
    }

    private fun initClickListeners() {
        binding.playlistBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playlistShareBtn.setOnClickListener {
            sharePlaylist()
        }

        binding.playlistMoreInfoBtn.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.playlistDeleteTextBottomSheet.setOnClickListener {
            deletePlaylist()
        }

        binding.playlistShareTextBottomSheet.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }

        binding.playlistEditTextBottomSheet.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlistId)
            )
        }
    }

    private fun setObservers() {
        playlistViewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) {
            playlistName = it.name
            render(it)
        }

        playlistViewModel.observePlaylistTracks().observe(viewLifecycleOwner) {
            adapter.setNewTracks(it)
        }

        playlistViewModel.observeClickedTrack().observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_playlistFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(it)
            )
        }
    }

    private fun sharePlaylist() {
        // Норм ли такая проверка? Или как по ГОСТу делать
        if (adapter.isEmpty()) {
            val toastText = getString(R.string.playlist_no_tracks_to_share, playlistName)
            Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
            return
        }

        playlistViewModel.sharePlaylist()
    }

    private fun initAdapter() {
        adapter = PlaylistTrackAdapter(this::onTrackClick, this::onTrackLongClick)
        binding.playlistTrackRecycleView.adapter = adapter
    }

    private fun onTrackClick(trackId: Long) {
        playlistViewModel.onTrackClick(trackId)
    }

    private fun onTrackLongClick(trackId: Long) {
        binding.playlistTotalOverlay.isVisible = true

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.playlist_delete_track_dialog_title))
            .setNegativeButton(getString(R.string.playlist_delete_dialog_negative_btn)) { _, _ ->
                binding.playlistTotalOverlay.isVisible = false
            }
            .setPositiveButton(getString(R.string.playlist_delete_dialog_confirm_btn)) { _, _ ->
                playlistViewModel.deleteTrack(trackId)
                binding.playlistTotalOverlay.isVisible = false
            }
            .setOnCancelListener {
                binding.playlistTotalOverlay.isVisible = false
            }
            .show()
    }

    private fun deletePlaylist() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.playlist_delete_dialog_title, playlistName))
            .setNegativeButton(getString(R.string.playlist_delete_dialog_negative_btn)) { _, _ ->

            }
            .setPositiveButton(getString(R.string.playlist_delete_dialog_confirm_btn)) { _, _ ->
                playlistViewModel.deletePlaylist()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun setOnBackDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (menuBottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })
    }

    private fun setBottomSheet() {
        tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            tracksBottomSheetCallback = object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.playlistOverlay.isVisible = false
                        }

                        else -> {
                            binding.playlistOverlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val alpha = (slideOffset * 0.9).toFloat()
                    binding.playlistOverlay.alpha = alpha
                }
            }
            addBottomSheetCallback(tracksBottomSheetCallback)
        }

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistMenuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN

            menuBottomSheetCallback = object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.playlistTotalOverlay.isVisible = true
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.playlistTotalOverlay.isVisible = false
                        }

                        else -> {

                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset < 0) {
                        binding.playlistTotalOverlay.alpha = (1 - abs(slideOffset))
                    }
                }
            }
            addBottomSheetCallback(menuBottomSheetCallback)
        }
    }

    private fun render(playlist: PlaylistDetailsInfo) {
        with(binding) {
            playlistName.text = playlist.name

            if (playlist.description.isNullOrEmpty()) {
                playlistDescription.isVisible = false
            } else {
                playlistDescription.isVisible = true
                playlistDescription.text = playlist.description
            }

            playlistTotalDuration.text = resources.getQuantityString(
                R.plurals.plurals_minutes,
                playlist.totalDuration,
                playlist.totalDuration
            )

            playlistNumberOfTracks.text = resources.getQuantityString(
                R.plurals.plurals_playlists_tracks,
                playlist.trackCount,
                playlist.trackCount
            )

            Glide.with(this@PlaylistFragment)
                .load(playlist.coverUri)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .into(playlistCover)
        }

        setMenuBottomSheetInfo(playlist)
    }

    private fun setMenuBottomSheetInfo(playlist: PlaylistDetailsInfo) {
        binding.playlistInfoBottomSheet.apply {
            playlistBottomSheetTitle.text = playlist.name
            playlistBottomSheetTrackCount.text = resources.getQuantityString(
                R.plurals.plurals_playlists_tracks,
                playlist.trackCount,
                playlist.trackCount
            )

            Glide.with(this@PlaylistFragment)
                .load(playlist.coverUri)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        dpToPx(
                            BOTTOM_SHEET_PLAYLIST_RADIUS, requireContext()
                        )
                    )
                )
                .into(playlistBottomSheetCover)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        tracksBottomSheetBehavior.removeBottomSheetCallback(tracksBottomSheetCallback)
        menuBottomSheetBehavior.removeBottomSheetCallback(menuBottomSheetCallback)
    }

    companion object {
        private const val BOTTOM_SHEET_PLAYLIST_RADIUS = 2f
        private const val ARG_PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long) = bundleOf(ARG_PLAYLIST_ID to playlistId)
    }
}