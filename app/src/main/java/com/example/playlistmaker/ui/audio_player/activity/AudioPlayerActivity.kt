package com.example.playlistmaker.ui.audio_player.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.audio_player.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.ui.util.dpToPx
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var currentTrack: TrackDetailsInfo

    private lateinit var viewModel: AudioPlayerViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.audioPlayer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentTrack = intent.getParcelableExtra(TRACK_DETAILS_INFO, TrackDetailsInfo::class.java)!!
        viewModel = getViewModel(parameters = { parametersOf(currentTrack.musicUrl) })
        viewModel.prepare()

        setViewModelObservers()
        initClickListeners()
        setUI()
    }

    private fun setViewModelObservers() {
        viewModel.observeTimer().observe(this) {
            binding.playerTrackCurrentTime.text = it
        }

        viewModel.observePlayerState().observe(this) {
            when (it) {
                MediaPlayerState.STATE_DEFAULT -> {
                    binding.playerPlayBtn.isEnabled = false
                }

                MediaPlayerState.STATE_PREPARED -> {
                    binding.playerPlayBtn.isEnabled = true
                    binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_play_btn_83)
                }

                MediaPlayerState.STATE_PLAYING -> {
                    binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_pause_btn_83)
                }

                MediaPlayerState.STATE_PAUSED -> {
                    binding.playerPlayBtn.setImageResource(R.drawable.ic_audio_player_play_btn_83)
                }
            }
        }
    }

    private fun setUI() {
        binding.apply {
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
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, this)))
            .into(binding.playerTrackCover)
    }

    private fun setReleaseDate() {
        binding.apply {
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
        binding.apply {
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

    private fun initClickListeners() {
        binding.apply {
            playerBackButton.setOnClickListener {
                finish()
            }

            playerPlayBtn.setOnClickListener {
                viewModel.changeState()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }


    companion object {
        private const val CORNER_RADIUS = 8f
        private const val TRACK_DETAILS_INFO = "track_details"

        fun show(track: TrackDetailsInfo, context: Context) {
            val intent = Intent(context, AudioPlayerActivity::class.java).apply {
                putExtra(TRACK_DETAILS_INFO, track)
            }

            context.startActivity(intent)
        }
    }
}