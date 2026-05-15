package com.example.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.audio_player.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.util.State
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.playlistmaker.ui.search.viewmodel.model.EditTextState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.playlistmaker.ui.search.model.TrackDetailsInfo
import com.example.playlistmaker.ui.search.viewmodel.model.SearchConstants
import com.example.playlistmaker.ui.util.theme.AppDimens
import com.example.playlistmaker.ui.util.theme.AppTextStyles
import com.example.playlistmaker.ui.util.theme.AppTheme
import com.example.playlistmaker.ui.util.theme.colors

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    SearchScreenContent(
                        viewModel = searchViewModel,
                        onTrackClick = { trackDetailsInfo ->
                            findNavController().navigate(
                                R.id.action_searchFragment_to_audioPlayerFragment,
                                AudioPlayerFragment.createArgs(trackDetailsInfo)
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.setConstants(SearchConstants(requireContext()))
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.performSearch()
    }
}

@Composable
fun SearchScreenContent(
    viewModel: SearchViewModel,
    onTrackClick: (TrackDetailsInfo) -> Unit
) {
    val editTextState by viewModel.observeEditTextValue().observeAsState(EditTextState())
    val searchState by viewModel.observeSearchState().observeAsState()
    val historyTracks by viewModel.observeHistory().observeAsState(emptyList())
    val trackClickEvent by viewModel.observeOnTrackClick().observeAsState()

    LaunchedEffect(trackClickEvent) {
        trackClickEvent?.let { trackDetailsInfo ->
            onTrackClick(trackDetailsInfo)
            viewModel.resetClickedTrack()
        }
    }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = editTextState.text ?: "",
                selection = TextRange(editTextState.text?.length ?: 0)
            )
        )
    }

    var isTextFieldFocused by remember { mutableStateOf(editTextState.isFocused) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(textFieldValue.text) {
        if (textFieldValue.text != editTextState.text) {
            viewModel.onTextChanged(textFieldValue.text)
        }
    }

    LaunchedEffect(isTextFieldFocused) {
        if (isTextFieldFocused != editTextState.isFocused) {
            viewModel.setFocus(isTextFieldFocused)
        }
    }

    LaunchedEffect(editTextState.text) {
        if (editTextState.text != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(
                text = editTextState.text ?: "",
                selection = TextRange(editTextState.text?.length ?: 0)
            )
        }
    }

    LaunchedEffect(editTextState.isFocused) {
        if (editTextState.isFocused != isTextFieldFocused) {
            isTextFieldFocused = editTextState.isFocused
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors().colorSecondary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.search_hint),
                style = AppTextStyles.mediumText,
                color = colors().colorOnSecondary,
                modifier = Modifier.padding(
                    horizontal = AppDimens.normalPadding,
                    vertical = AppDimens.minorPadding
                )
            )
            Box(
                modifier = Modifier
                    .padding(AppDimens.normalPadding)
                    .height(AppDimens.searchHeight)
                    .clip(RoundedCornerShape(AppDimens.cornerRadius8))
                    .background(colors().lightGrayToWhite)
            ) {
                SearchEditText(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    onSearch = { viewModel.performSearch() },
                    onFocusChange = { isFocused ->
                        isTextFieldFocused = isFocused
                    },
                    onClearClick = {
                        textFieldValue = TextFieldValue("")
                        viewModel.onTextChanged("")
                        keyboardController?.hide()
                    }
                )
            }

            if (!editTextState.text.isNullOrEmpty()) {
                when (val state = searchState) {
                    State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colors().colorSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(44.dp),
                                color = colors().blue,
                                strokeWidth = 4.dp
                            )
                        }
                    }

                    is State.Content<*> -> {
                        SearchResultsList(
                            tracks = state.data as List<TrackInfo>,
                            viewModel = viewModel
                        )
                    }

                    is State.Empty -> {
                        PlaceholderLayout(
                            icon = R.drawable.no_tracks_found_placeholder,
                            text = state.message,
                            showButton = false
                        )
                    }

                    is State.Error -> {
                        PlaceholderLayout(
                            icon = R.drawable.connection_error_placeholder,
                            text = state.message,
                            showButton = true,
                            onRefresh = { viewModel.performSearch() }
                        )
                    }

                    null -> {

                    }
                }
            } else {
                if (historyTracks.isNotEmpty()) {
                    SearchHistorySection(
                        historyTracks = historyTracks,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun SearchEditText(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSearch: () -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onClearClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppDimens.normalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.search_icon_16x16),
            contentDescription = null,
            tint = colors().vectorColor,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(AppDimens.minorPadding))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    onFocusChange(focusState.isFocused)
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSearch() }
            ),
            cursorBrush = SolidColor(colors().blue),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_hint),
                            style = AppTextStyles.regularText,
                            color = colors().vectorColor
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (value.text.isNotEmpty()) {
            Spacer(modifier = Modifier.width(AppDimens.minorPadding))
            Icon(
                painter = painterResource(R.drawable.close_button_search),
                contentDescription = "Clear",
                tint = colors().vectorColor,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onClearClick() }
            )
        }
    }
}

@Composable
fun SearchResultsList(
    tracks: List<TrackInfo>,
    viewModel: SearchViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = AppDimens.normalPadding)
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

@Composable
fun SearchHistorySection(
    historyTracks: List<TrackInfo>,
    viewModel: SearchViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (historyTracks.isNotEmpty()) {
            Text(
                text = stringResource(R.string.search_history_title),
                style = AppTextStyles.searchPlaceholder,
                color = colors().colorOnSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppDimens.normalPadding),
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = AppDimens.normalPadding)
        ) {
            items(
                items = historyTracks,
                key = { it.id }
            ) { track ->
                TrackItem(
                    track = track,
                    onClick = { viewModel.onTrackClicked(track.id) }
                )
            }

            if (historyTracks.isNotEmpty()) {
                item {
                    Button(
                        onClick = { viewModel.clearHistory() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(top = AppDimens.bigPadding),
                        shape = RoundedCornerShape(AppDimens.buttonRadius54),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colors().colorOnSecondary,
                            contentColor = colors().colorSecondary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.search_clear_history),
                            style = AppTextStyles.funcButtonText,
                            color = colors().colorSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrackItem(
    track: TrackInfo,
    onClick: () -> Unit
) {
    val colors = colors()
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = AppDimens.miniPadding),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.minorPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(track.artworkUrl100)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_placeholder_45),
            placeholder = painterResource(R.drawable.ic_placeholder_45)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = track.title,
                style = AppTextStyles.regularText,
                color = colors.colorOnSecondary,
                maxLines = 1
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = track.artistName,
                    style = AppTextStyles.miniRegularText,
                    color = colors.grayToWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = AppDimens.miniPadding)
                        .size(3.dp)
                        .background(color = colors.grayToWhite, shape = CircleShape)
                )

                Text(
                    text = track.time,
                    style = AppTextStyles.miniRegularText,
                    color = colors.grayToWhite,
                    maxLines = 1
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.mini_arrow),
            contentDescription = null,
            tint = colors.gray
        )
    }
}

@Composable
fun PlaceholderLayout(
    icon: Int,
    text: String,
    showButton: Boolean,
    onRefresh: (() -> Unit)? = null
) {
    val colors = colors()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppDimens.normalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(AppDimens.normalPadding))

        Text(
            text = text,
            style = AppTextStyles.searchPlaceholder,
            color = colors.black,
            textAlign = TextAlign.Center
        )

        if (showButton && onRefresh != null) {
            Spacer(modifier = Modifier.height(AppDimens.normalPadding))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(AppDimens.normalButtonRadius))
                    .background(colors.blue)
                    .clickable { onRefresh() }
                    .padding(
                        horizontal = AppDimens.normalPadding,
                        vertical = AppDimens.minorPadding
                    )
            ) {
                Text(
                    text = stringResource(R.string.search_refresh),
                    style = AppTextStyles.funcButtonText,
                    color = colors.white
                )
            }
        }
    }
}