package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.media.model.PlaylistDetails
import com.example.playlistmaker.ui.playlist.fragment.PlaylistFragment
import com.example.playlistmaker.ui.util.theme.AppDimens
import com.example.playlistmaker.ui.util.theme.AppTextStyles
import com.example.playlistmaker.ui.util.theme.AppTheme
import com.example.playlistmaker.ui.util.theme.colors
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme(isSystemInDarkTheme()) {
                    PlaylistsScreenContent(
                        viewModel = playlistsViewModel,
                        onCreatePlaylistClick = {
                            findNavController().navigate(
                                R.id.action_mediaFragment_to_newPlaylistFragment
                            )
                        },
                        onPlaylistClick = { playlistId ->
                            findNavController().navigate(
                                R.id.action_mediaFragment_to_playlistFragment,
                                PlaylistFragment.createArgs(playlistId)
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.loadPlaylists()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}

@Composable
fun PlaylistsScreenContent(
    viewModel: PlaylistsViewModel,
    onCreatePlaylistClick: () -> Unit,
    onPlaylistClick: (Long) -> Unit
) {
    val playlists by viewModel.observePlaylists().observeAsState(emptyList())
    val clickedPlaylistId by viewModel.observeClickedPlaylist().observeAsState()

    LaunchedEffect(clickedPlaylistId) {
        clickedPlaylistId?.let { playlistId ->
            onPlaylistClick(playlistId)
            viewModel.onPlaylistClickedConsumed()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors().colorSecondary)
    ) {
        Button(
            onClick = onCreatePlaylistClick,
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = AppDimens.bigPadding),
            shape = RoundedCornerShape(AppDimens.buttonRadius54),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors().colorOnSecondary,
                contentColor = colors().colorSecondary
            )
        ) {
            Text(
                text = stringResource(R.string.media_new_playlist),
                style = AppTextStyles.funcButtonText,
                color = colors().colorSecondary
            )
        }

        if (playlists.isEmpty()) {
            PlaceholderPlaylists()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = AppDimens.normalPadding,
                    end = AppDimens.normalPadding,
                    top = AppDimens.normalPadding,
                    bottom = AppDimens.normalPadding
                ),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.normalPadding),
                verticalArrangement = Arrangement.spacedBy(AppDimens.normalPadding)
            ) {
                items(
                    items = playlists,
                    key = { it.id }
                ) { playlist ->
                    PlaylistItem(
                        playlist = playlist,
                        onClick = { viewModel.onPlaylistClick(playlist.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: PlaylistDetails,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(playlist.coverUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(AppDimens.cornerRadius8)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_placeholder_45),
            placeholder = painterResource(R.drawable.ic_placeholder_45)
        )

        Spacer(modifier = Modifier.height(AppDimens.minorPadding))

        Text(
            text = playlist.name,
            style = AppTextStyles.regularText,
            color = colors().colorOnSecondary,
            maxLines = 1
        )

        Text(
            text = context.resources.getQuantityString(
                R.plurals.plurals_playlists_tracks,
                playlist.trackCount,
                playlist.trackCount
            ),
            style = AppTextStyles.miniRegularText,
            color = colors().grayToWhite
        )
    }
}

@Composable
fun PlaceholderPlaylists() {
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
            text = stringResource(R.string.media_no_playlist_found),
            style = AppTextStyles.searchPlaceholder,
            color = colors().colorOnSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(0.8f))
    }
}