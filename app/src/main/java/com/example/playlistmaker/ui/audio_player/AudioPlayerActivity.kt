package com.example.playlistmaker.ui.audio_player

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.presentation.model.TrackDetailsInfo
import com.example.playlistmaker.ui.util.dpToPx

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var currentTrack: TrackDetailsInfo

    private lateinit var mediaPlayer: MyMediaPlayer

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

        mediaPlayer = MyMediaPlayer(binding, currentTrack.musicUrl, this)
        initClickListeners()
        setUI()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun setUI() {
        setImage(currentTrack.artworkUrl512)
        binding.playerTrackTitle.text = currentTrack.title
        binding.playerTrackArtist.text = currentTrack.artistName
        binding.playerTrackCurrentTime.text = getString(R.string.default_timer_value)
        binding.playerTrackDuration.text = currentTrack.time

        if (currentTrack.collectionName != null) {
            binding.playerTrackCollection.text = currentTrack.collectionName
            binding.playerTrackCollection.visibility = View.VISIBLE
            binding.playerTrackCollectionName.visibility = View.VISIBLE

        } else {
            binding.playerTrackCollection.visibility = View.GONE
            binding.playerTrackCollectionName.visibility = View.GONE
        }

        if (currentTrack.releaseDate != null) {
            binding.playerReleaseYear.text = currentTrack.releaseDate
            binding.playerReleaseYear.visibility = View.VISIBLE
            binding.playerReleaseYearName.visibility = View.VISIBLE
        } else {
            binding.playerReleaseYear.visibility = View.GONE
            binding.playerReleaseYearName.visibility = View.GONE
        }

        binding.playerGenre.text = currentTrack.primaryGenreName
        binding.playerCountry.text = currentTrack.country
    }

    private fun setImage(artUrl: String) {
        val trackCover = binding.playerTrackCover

        Glide.with(this)
            .load(artUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, this)))
            .into(trackCover)
    }

    private fun initClickListeners() {
        binding.playerBackButton.setOnClickListener {
            finish()
        }

        binding.playerPlayBtn.setOnClickListener {
            mediaPlayer.onClickPlayBtn()
        }
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