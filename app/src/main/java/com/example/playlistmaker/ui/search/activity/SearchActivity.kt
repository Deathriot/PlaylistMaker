package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.ui.search.model.State
import com.example.playlistmaker.ui.search.model.TrackInfo
import com.example.playlistmaker.ui.search.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.audio_player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.SearchTrackAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var editText: EditText
    private lateinit var textWatcher: TextWatcher
    private lateinit var adapter: SearchTrackAdapter

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this, SearchViewModel.getFactory()
        )[SearchViewModel::class.java]

        binding = ActivitySearchBinding.inflate(layoutInflater)
        editText = binding.searchEditText
        adapter = SearchTrackAdapter(this::onTrackClick)
        binding.searchRecycleView.adapter = adapter

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setObservers()
        initClickListeners()
        setEditTextActions()
    }

    private fun setObservers() {
        viewModel.observeEditTextValue().observe(this) {
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

        viewModel.observeSearchState().observe(this) {
            render(it)
        }

        viewModel.observeOnTrackClick().observe(this) {
            AudioPlayerActivity.show(it, this)
        }

        viewModel.observeHistory().observe(this) {
            adapter.setTracks(it)
            if (it.isEmpty()) {
                hideHistory()
            }
        }
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
        viewModel.clearHistory()
        adapter.setTracks(emptyList())
    }

    private fun showHistory() {
        if (adapter.isEmpty()) {
            return
        }

        binding.apply {
            searchHistoryTitle.isVisible = true
            searchClearHistory.isVisible = true
            searchRecycleView.isVisible = true
        }
    }

    private fun hideHistory() {
        binding.apply {
            binding.searchHistoryTitle.isVisible = false
            binding.searchClearHistory.isVisible = false
            binding.searchRecycleView.isVisible = false
        }
    }

    private fun onTrackClick(trackId: Long) {
        viewModel.onTrackClicked(trackId)
    }

    private fun setEditTextActions() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        editText.addTextChangedListener(textWatcher)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search()
            }
            false
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.setFocus(hasFocus)
        }
    }

    private fun initClickListeners() {
        binding.btnSettingsBack.setOnClickListener {
            finish()
        }

        binding.searchRefreshButton.setOnClickListener {
            viewModel.search()
        }

        binding.searchClearHistory.setOnClickListener {
            clearSearchHistory()
            hideHistory()
        }

        binding.btnClearTextSearch.setOnClickListener {
            viewModel.onTextChanged("")
            binding.searchPlaceholderLayout.isVisible = false
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()

        editText.setSelection(editText.text.length)
    }

    override fun onDestroy() {
        super.onDestroy()
        editText.removeTextChangedListener(textWatcher)
    }
}