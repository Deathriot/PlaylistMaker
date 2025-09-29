package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.util.dpToPx
import java.time.ZonedDateTime

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var currentTrack : Track
    private val CORNER_RADIUS = 8f

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

         currentTrack = intent.getParcelableExtra(App.SEARCH_NEW_TRACK_KEY, Track::class.java)!!

        setBackBtn()
        setUI()
    }

    private fun setUI(){
        setImage()
        binding.playerTrackTitle.text = currentTrack.title
        binding.playerTrackArtist.text = currentTrack.artistName
        binding.playerTrackCurrentTime.text = "0:00"
        binding.playerTrackDuration.text = currentTrack.time

        if(currentTrack.collectionName != null) {
            binding.playerTrackCollection.text = currentTrack.collectionName
            binding.playerTrackCollection.visibility = View.VISIBLE
            binding.playerTrackCollectionName.visibility = View.VISIBLE

        } else {
            binding.playerTrackCollection.visibility = View.GONE
            binding.playerTrackCollectionName.visibility = View.GONE
        }

        if(currentTrack.releaseDate != null){
            val year = ZonedDateTime.parse(currentTrack.releaseDate).year
            binding.playerReleaseYear.text = year.toString()
            binding.playerReleaseYear.visibility = View.VISIBLE
            binding.playerReleaseYearName.visibility = View.VISIBLE
        } else {
            binding.playerReleaseYear.visibility = View.GONE
            binding.playerReleaseYearName.visibility = View.GONE
        }

        binding.playerGenre.text = currentTrack.primaryGenreName
        binding.playerCountry.text = currentTrack.country
    }

    private fun setImage(){
        val artUrl = currentTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
        val trackCover = binding.playerTrackCover

        Glide.with(this)
            .load(artUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(CORNER_RADIUS, this)))
            .into(trackCover)
    }

    private fun setBackBtn() {
        binding.playerBackButton.setOnClickListener {
            finish()
        }
    }
}