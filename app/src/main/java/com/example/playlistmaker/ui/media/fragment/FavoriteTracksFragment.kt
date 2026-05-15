package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.audio_player.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.media.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.ui.search.fragment.TrackItem
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.util.State
import com.example.playlistmaker.ui.util.theme.AppDimens
import com.example.playlistmaker.ui.util.theme.AppTextStyles
import com.example.playlistmaker.ui.util.theme.AppTheme
import com.example.playlistmaker.ui.util.theme.colors
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme(isSystemInDarkTheme()) {
                    FavoriteTracksScreenContent(
                        viewModel = favoriteTracksViewModel,
                        onTrackClick = { trackDetailsInfo ->
                            findNavController().navigate(
                                R.id.action_mediaFragment_to_audioPlayerFragment,
                                AudioPlayerFragment.createArgs(trackDetailsInfo)
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.loadTracks()
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}

@Composable
fun FavoriteTracksScreenContent(
    viewModel: FavoriteTracksViewModel,
    onTrackClick: (TrackDetailsInfo) -> Unit
) {
    val state by viewModel.observeState().observeAsState()
    val trackClickEvent by viewModel.observeTrackDetails().observeAsState()

    LaunchedEffect(trackClickEvent) {
        trackClickEvent?.let { trackDetailsInfo ->
            onTrackClick(trackDetailsInfo)
            viewModel.onTrackClickedConsumed()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = AppDimens.normalPadding)
    ) {
        when (state) {
            is State.Content<*> -> {
                val tracks = (state as State.Content<*>).data as List<TrackInfo>
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = tracks,
                        key = { it.id }
                    ) { track ->
                        TrackItem(
                            track = track,
                            onClick = { viewModel.onTrackClicked(track.id) }
                        )
                    }
                }
            }

            else -> {
                PlaceholderLayout()
            }
        }
    }
}

@Composable
fun PlaceholderLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppDimens.normalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.2f))

        Icon(
            painter = painterResource(R.drawable.no_tracks_found_placeholder),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(AppDimens.normalPadding))

        Text(
            text = stringResource(R.string.media_is_empty),
            style = AppTextStyles.searchPlaceholder,
            color = colors().colorOnSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(0.8f))
    }
}