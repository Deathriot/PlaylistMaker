package com.example.playlistmaker.ui.audio_player.fragment

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.ui.audio_player.PlayerPlaylistAdapter
import com.example.playlistmaker.ui.audio_player.model.PlayerState
import com.example.playlistmaker.ui.audio_player.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.util.dpToPx
import com.example.playlistmaker.utils.broadcast_receiver.InternetConnectionBroadcastReceiver
import com.example.playlistmaker.utils.services.media_player_service.MediaPlayerService
import com.example.playlistmaker.utils.services.media_player_service.converter.ServiceTrackDataConverter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class AudioPlayerFragment : Fragment() {

    private var hasNotification = false
    private var requester: PermissionRequester = PermissionRequester.instance()

    private lateinit var currentTrack: TrackDetailsInfo
    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetCallback: BottomSheetCallback
    private lateinit var adapter: PlayerPlaylistAdapter

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private val internetConnectionBroadcastReceiver: InternetConnectionBroadcastReceiver by inject()

    private var mediaPlayerService: MediaPlayerService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MediaPlayerServiceBinder
            mediaPlayerService = binder.getService()
            viewModel.setMediaPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeMediaPlayerControl()
            mediaPlayerService = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNotificationPermission()
        currentTrack = requireArguments().getParcelable(ARGS_TRACKS, TrackDetailsInfo::class.java)!!

        viewModel = getViewModel(parameters = { parametersOf(currentTrack) })
        viewModel.prepare()

        bindMediaPlayerService()

        setBottomSheet()
        initAdapter()
        setObservers()
        initClickListeners()
        setUI()
    }

    private fun initClickListeners() {
        with(binding) {
            playerBackButton.setOnClickListener {
                findNavController().navigateUp()
            }

            playerPlayBtn.setOnClickListener {
                viewModel.changeState()
            }

            playerLikeBtn.setOnClickListener {
                viewModel.changeLikeState()
            }

            playerAddPlaylistBtn.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            playerBottomSheetAddPlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
            }
        }
    }

    private fun setObservers() {
        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            onPlayerState(it.playerState)
            onLike(it.isLiked)
        }

        viewModel.observePlaylists().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeIsTrackAdded().observe(viewLifecycleOwner) {
            if (it.isAdded) {
                viewModel.loadPlaylists()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.player_added_to_playlist, it.name),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.player_already_added_to_playlist, it.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun bindMediaPlayerService() {
        val intent = Intent(requireContext(), MediaPlayerService::class.java).apply {
            val serviceTrackData = ServiceTrackDataConverter.convertToServiceTrackData(currentTrack)
            putExtra(MediaPlayerService.SERVICE_TRACK_ARGS, serviceTrackData)
        }

        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unBindMediaPlayerService() {
        requireContext().unbindService(serviceConnection)
    }

    private fun initAdapter() {
        adapter = PlayerPlaylistAdapter(this::onPlaylistClick)
        binding.playerBottomSheetRecycleView.adapter = adapter
    }

    private fun onPlaylistClick(playlistId: Long, playlistName: String) {
        viewModel.addTrackToPlaylist(playlistId, playlistName)
    }

    private fun render(playlists: List<PlaylistDetails>) {
        adapter.setContent(playlists)
    }

    private fun onLike(isLiked: Boolean) {
        if (isLiked) {
            binding.playerLikeBtn.setImageResource(R.drawable.ic_audio_player_active_like_btn_51)
        } else {
            binding.playerLikeBtn.setImageResource(R.drawable.ic_audio_player_inactive_like_btn_51)
        }
    }

    private fun onPlayerState(playerState: PlayerState) {
        binding.playerTrackCurrentTime.text = playerState.progress
        binding.playerPlayBtn.isEnabled = playerState.enabled

        if (playerState is PlayerState.Playing) {
            binding.playerPlayBtn.changeState(true)
        } else {
            binding.playerPlayBtn.changeState(false)
        }

    }

    private fun setUI() {
        with(binding) {
            setImage(currentTrack.artworkUrl512)
            playerTrackTitle.text = currentTrack.title
            playerTrackArtist.text = currentTrack.artistName
            playerTrackCurrentTime.text = getString(R.string.default_timer_value)
            playerTrackDuration.text = currentTrack.time
            setCollectionName()
            setReleaseDate()
            binding.playerGenre.text = currentTrack.primaryGenreName
            binding.playerCountry.text = currentTrack.country
        }
    }

    private fun setImage(artUrl: String) {
        Glide.with(this)
            .load(artUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, requireContext())))
            .into(binding.playerTrackCover)
    }

    private fun setReleaseDate() {
        with(binding) {
            if (currentTrack.releaseDate != null) {
                playerReleaseYear.text = currentTrack.releaseDate
                playerReleaseYear.isVisible = true
                playerReleaseYearName.isVisible = true
            } else {
                playerReleaseYear.isVisible = false
                playerReleaseYearName.isVisible = false
            }
        }
    }

    private fun setCollectionName() {
        with(binding) {
            if (currentTrack.collectionName != null) {
                playerTrackCollection.text = currentTrack.collectionName
                playerTrackCollection.isVisible = true
                playerTrackCollectionName.isVisible = true
            } else {
                playerTrackCollection.isVisible = false
                playerTrackCollectionName.isVisible = false
            }
        }
    }


    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetCallback = object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.playerOverlay.isVisible = false
                        }

                        else -> {
                            binding.playerOverlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    calculateOverlayAlpha(slideOffset)
                }
            }
            addBottomSheetCallback(bottomSheetCallback)
        }
    }

    private fun calculateOverlayAlpha(slideOffset: Float) {
        when {
            slideOffset >= 0 -> {
                val alpha = 0.6 + slideOffset * 0.4
                binding.playerOverlay.alpha = alpha.toFloat()
            }

            else -> {
                val alpha = (1 - abs(slideOffset)) * 0.6
                binding.playerOverlay.alpha = alpha.toFloat()
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotification = requester.isAnyGranted(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotification = true
        }

        getNotificationPermission()
    }

    private fun getNotificationPermission() {
        if (!hasNotification) {
            lifecycleScope.launch {
                val result = requester.request(Manifest.permission.POST_NOTIFICATIONS).first()
                when (result) {
                    is PermissionResult.Granted -> hasNotification = true
                    is PermissionResult.Denied.NeedsRationale -> createRationalNotificationDialog()

                    is PermissionResult.Denied.DeniedPermanently -> {}
                    PermissionResult.Cancelled -> {}
                }
            }
        }
    }


    private fun createRationalNotificationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.player_dialog_notification_rationale_title))
            .setMessage(getString(R.string.player_dialog_notification_rationale_message))
            .setPositiveButton(getString(R.string.player_dialog_notification_rationale_confirm_btn)) { _, _ ->
                getNotificationPermission()
            }
            .setNegativeButton(getString(R.string.player_dialog_notification_rationale_decline_btn)) { _, _ ->

            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mediaPlayerService?.stopForeGround()
        requireActivity().registerReceiver(
            internetConnectionBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        if (hasNotification) {
            mediaPlayerService?.startForeground()
        } else {
            mediaPlayerService?.releasePlayer()
        }

        requireActivity().unregisterReceiver(internetConnectionBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
        unBindMediaPlayerService()
        _binding = null
    }

    companion object {
        private const val CORNER_RADIUS = 8f

        private const val ARGS_TRACKS = "track_details"
        fun createArgs(track: TrackDetailsInfo) = bundleOf(ARGS_TRACKS to track)
    }
}