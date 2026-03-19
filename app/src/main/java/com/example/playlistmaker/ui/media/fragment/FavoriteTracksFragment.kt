package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.ui.audio_player.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.media.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.ui.root.activity.RootActivity
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.util.Debouncer
import com.example.playlistmaker.ui.util.State
import com.example.playlistmaker.ui.util.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private lateinit var adapter: TrackAdapter
    private lateinit var onTrackClickDebounce: (Long) -> Unit


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setObservers()
    }

    private fun setObservers() {
        favoriteTracksViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        favoriteTracksViewModel.observeTrackDetails().observe(viewLifecycleOwner){
            findNavController().navigate(
                R.id.action_mediaFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(it)
            )
        }
    }

    private fun render(state: State) {
        when (state) {
            is State.Empty -> {
                showPlaceHolder()
            }

            is State.Content<*> -> {
                adapter.setNewTracks(state.data as List<TrackInfo>)
                showContent()
            }

            is State.Error -> {

            }

            State.Loading -> {

            }
        }
    }

    private fun showPlaceHolder() {
        binding.favTracksPlaceholder.isVisible = true
        binding.mediaFavTracksRecycleView.isVisible = false
    }

    private fun showContent() {
        binding.favTracksPlaceholder.isVisible = false
        binding.mediaFavTracksRecycleView.isVisible = true
    }

    private fun initAdapter() {
        onTrackClickDebounce =
            Debouncer.debounce(
                RootActivity.BOTTOM_NAV_ANIMATION_DURATION,
                viewLifecycleOwner.lifecycleScope, false
            ) { trackId ->
                onTrackClick(trackId)
            }

        adapter = TrackAdapter{
            id -> onTrackClickDebounce(id)
        }

        binding.mediaFavTracksRecycleView.adapter = adapter
    }

    private fun onTrackClick(id: Long) {
        favoriteTracksViewModel.onTrackClicked(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.loadTracks()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}