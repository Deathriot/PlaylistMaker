package com.example.playlistmaker.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ClearHistoryButtonBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.ui.audio_player.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.root.activity.RootActivity
import com.example.playlistmaker.ui.search.SearchTrackAdapter
import com.example.playlistmaker.ui.search.model.State
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.search.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.search.viewmodel.model.SearchConstants
import com.example.playlistmaker.ui.util.Debouncer
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var _clearBinding: ClearHistoryButtonBinding? = null
    private val clearBinding get() = _clearBinding!!

    private val searchViewModel: SearchViewModel by viewModel()

    private lateinit var onTrackClickDebounce: (Long) -> Unit

    private lateinit var editText: EditText
    private lateinit var textWatcher: TextWatcher
    private lateinit var adapter: SearchTrackAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.setConstants(SearchConstants(requireContext()))
        initAdapter()
        initEditText()
        initClickListeners()
        setObservers()
    }

    private fun setObservers() {
        searchViewModel.observeEditTextValue().observe(viewLifecycleOwner) {
            val text = it.text
            val isFocused = it.isFocused


            if (editText.text.toString() != text) {
                editText.setText(text)
            }

            if (text.isNullOrEmpty()) {
                binding.btnClearTextSearch.isVisible = false
                if (isFocused) {
                    showHistory()
                } else {
                    hideHistory()
                }
            } else {
                hideHistory()
                binding.btnClearTextSearch.isVisible = true
            }
        }

        searchViewModel.observeSearchState().observe(viewLifecycleOwner) {
            render(it)
        }

        searchViewModel.observeOnTrackClick().observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(it)
            )
        }

        searchViewModel.observeHistory().observe(viewLifecycleOwner) {
            adapter.setHistoryTracks(it)
            if (it.isEmpty()) {
                hideHistory()
            }
        }
    }

    private fun initAdapter() {
        onTrackClickDebounce =
            Debouncer.debounce(
                RootActivity.BOTTOM_NAV_ANIMATION_DURATION,
                viewLifecycleOwner.lifecycleScope, false
            ) { trackId ->
                onTrackClick(trackId)
            }

        adapter = SearchTrackAdapter(
            onClick =
            { trackId ->
                onTrackClickDebounce(trackId)
            },
            clearBinding = clearBinding
        )

        binding.searchRecycleView.adapter = adapter
    }

    private fun render(state: State) {
        hideProgressBar()

        when (state) {
            is State.Content<*> -> {
                showTrackList(state.data as List<TrackInfo>)
            }

            is State.Empty -> {
                showTracksNotFound(state.message)
            }

            is State.Error -> {
                showInternetError(state.message)
            }

            State.Loading -> {
                showProgressBar()
            }
        }
    }

    private fun showProgressBar() {
        binding.searchProgressBar.visibility = View.VISIBLE
        binding.searchPlaceholderLayout.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE
        binding.searchRefreshButton.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.searchProgressBar.visibility = View.GONE
    }

    private fun showTrackList(tracks: List<TrackInfo>) {
        adapter.setTracks(tracks)
        binding.searchRecycleView.visibility = View.VISIBLE
        binding.searchPlaceholderLayout.visibility = View.GONE
    }

    private fun showInternetError(message: String) {
        binding.searchRecycleView.visibility = View.GONE
        binding.searchPlaceholderLayout.visibility = View.VISIBLE
        binding.searchRefreshButton.visibility = View.VISIBLE
        binding.searchPlaceholderText.text = message
        binding.searchPlaceholderIcon.setImageResource(R.drawable.connection_error_placeholder)
    }

    private fun showTracksNotFound(message: String) {
        binding.searchRecycleView.visibility = View.GONE
        binding.searchPlaceholderLayout.visibility = View.VISIBLE
        binding.searchRefreshButton.visibility = View.GONE
        binding.searchPlaceholderText.text = message
        binding.searchPlaceholderIcon.setImageResource(R.drawable.no_tracks_found_placeholder)
    }

    private fun clearSearchHistory() {
        searchViewModel.clearHistory()
        adapter.setTracks(emptyList())
    }

    private fun showHistory() {
        if (adapter.isEmpty()) {
            return
        }

        binding.apply {
            searchHistoryTitle.isVisible = true
            searchRecycleView.isVisible = true
        }

        clearBinding.searchClearHistory.isVisible = true
    }

    private fun hideHistory() {
        binding.apply {
            binding.searchHistoryTitle.isVisible = false
            binding.searchRecycleView.isVisible = false
        }

        clearBinding.searchClearHistory.isVisible = false
    }

    private fun onTrackClick(trackId: Long) {
        searchViewModel.onTrackClicked(trackId)
    }

    private fun initEditText() {
        editText = binding.searchEditText

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchViewModel.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        editText.addTextChangedListener(textWatcher)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.performSearch()
            }
            false
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            searchViewModel.setFocus(hasFocus)
        }
    }

    private fun initClickListeners() {
        binding.searchRefreshButton.setOnClickListener {
            searchViewModel.performSearch()
        }

        clearBinding.searchClearHistory.setOnClickListener {
            clearSearchHistory()
            hideHistory()
        }


        binding.btnClearTextSearch.setOnClickListener {
            searchViewModel.onTextChanged("")
            binding.searchPlaceholderLayout.isVisible = false
            (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        editText.requestFocus()
        editText.setSelection(editText.text.length)
        searchViewModel.performSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editText.removeTextChangedListener(textWatcher)
        _binding = null
        _clearBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _clearBinding = ClearHistoryButtonBinding.inflate(inflater, container, false)
        return binding.root
    }
}
