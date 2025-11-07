package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.mapper.TrackDetailsInfoMapper
import com.example.playlistmaker.presentation.mapper.TrackInfoMapper
import com.example.playlistmaker.ui.audio_player.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {

    var editTextValue = ""
    private var lastSearch = ""
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistory: SearchHistory
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search(editTextValue) }

    private val getTracksUseCase = Creator.provideGetTracksUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        searchHistory = SearchHistory(binding)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initClickListeners()
        setEditTextActions(savedInstanceState)
        setRecyclerView()
        setSearchActions()
    }

    private fun setSearchActions() {
        val editText = binding.searchEditText

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val term = editText.text.toString()
                search(term)
            }
            false
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editText.text.isEmpty()) {
                searchHistory.showHistory()
            }
        }
    }

    private fun search(term: String) {
        if (term.isEmpty()) {
            return
        }

        lastSearch = term
        showProgressBar()
        handler.removeCallbacks(searchRunnable)

        getTracksUseCase.execute(
            title = term,
            consumer = object : Consumer<List<Track>?> {
                override fun consume(data: ConsumerData<List<Track>?>) {
                    changeUI(data)
                }
            })
    }

    private fun changeUI(data: ConsumerData<List<Track>?>) {
        hideProgressBar()
        Log.i("LOADING", "LOADING")
        when (data) {
            is ConsumerData.Data -> {
                if (data.value.isNullOrEmpty()) {
                    showTracksNotFound()
                    return
                }

                val tracksInfo = data.value.map { TrackInfoMapper.map(it) }
                val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter
                adapter.setTracks(tracksInfo)
                showTrackList()
            }

            is ConsumerData.Error -> {
                showInternetError()
                Log.i("search", data.message)
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

    private fun showTrackList() {
        binding.searchRecycleView.visibility = View.VISIBLE
        binding.searchPlaceholderLayout.visibility = View.GONE
    }

    private fun showInternetError() {
        binding.searchRecycleView.visibility = View.GONE
        binding.searchPlaceholderLayout.visibility = View.VISIBLE
        binding.searchRefreshButton.visibility = View.VISIBLE
        binding.searchPlaceholderText.text = getString(R.string.search_internet_error)
        binding.searchPlaceholderIcon.setImageResource(R.drawable.connection_error_placeholder)
    }

    private fun showTracksNotFound() {
        binding.searchRecycleView.visibility = View.GONE
        binding.searchPlaceholderLayout.visibility = View.VISIBLE
        binding.searchRefreshButton.visibility = View.GONE
        binding.searchPlaceholderText.text = getString(R.string.search_not_found)
        binding.searchPlaceholderIcon.setImageResource(R.drawable.no_tracks_found_placeholder)
    }

    private fun initClickListeners() {
        binding.btnSettingsBack.setOnClickListener({
            finish()
        })

        binding.searchRefreshButton.setOnClickListener({
            search(lastSearch)
        })
    }

    private fun setRecyclerView() {
        val adapter = SearchTrackAdapter(this::onTrackClick)
        binding.searchRecycleView.adapter = adapter
    }

    private fun onTrackClick(trackId: Long) {
        val track = getTracksUseCase.getById(trackId) ?:
        searchHistory.getTrackFromHistory(trackId) ?: return

        searchHistory.addTrack(track)
        val trackDetailsInfo = TrackDetailsInfoMapper.map(track)
        AudioPlayerActivity.show(trackDetailsInfo, this)
    }

    private fun setEditTextActions(savedInstanceState: Bundle?) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        val editText = binding.searchEditText
        val clearBtn = binding.btnClearTextSearch


        if (savedInstanceState != null) {
            editText.setText(editTextValue)
        }

        clearBtn.setOnClickListener{
            editText.setText("")
            editTextValue = ""
            lastSearch = ""
            val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter

            if (searchHistory.isHistoryEmpty()) {
                adapter.setTracks(emptyList())
                binding.searchRecycleView.visibility = View.GONE
            }

            binding.searchPlaceholderLayout.visibility = View.GONE
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = setButtonVisibility(s)

                if (!s.isNullOrEmpty()) {
                    editTextValue = s.toString()
                    searchDebounce()
                    searchHistory.hideHistory()
                } else {
                    if (editText.hasFocus()) {
                        searchHistory.showHistory()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        editText.addTextChangedListener(textWatcher)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_KEY, editTextValue)
    }

    override fun onResume() {
        super.onResume()
        val editText = binding.searchEditText
        editText.setText(editTextValue)
        editText.setSelection(editTextValue.length)

        if (editTextValue.isNotEmpty()) {
            binding.searchRecycleView.visibility = View.VISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextValue = savedInstanceState.getString(EDIT_TEXT_KEY, "")
    }

    private fun setButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val EDIT_TEXT_KEY = "editText"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}