package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
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
import com.example.playlistmaker.client.ITunesService
import com.example.playlistmaker.client.SearchStatus
import com.example.playlistmaker.client.SearchTrackResult
import com.example.playlistmaker.client.TrackResponse
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.util.SearchHistory
import com.example.playlistmaker.util.SearchTrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    var editTextValue = ""
    private var lastSearch = ""
    private lateinit var binding: ActivitySearchBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private val service = ITunesService().service
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search(editTextValue) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        prefs = getSharedPreferences(App.APP_SHARED_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(binding, prefs)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setButtonsAction()
        setEditTextActions(savedInstanceState)
        setRecyclerView()
        setSearchActions()
    }


    private fun setSearchActions() {
        val editText = binding.searchEditText

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val term = editText.text.toString()
                Log.i("TestTerm", term)
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
        if(term.isEmpty()){
            return
        }

        lastSearch = term
        showProgressBar()

        service.searchTrack(term).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                val tracks = response.body()?.tracks

                val result = SearchTrackResult(
                    tracks,
                    status = if (tracks.isNullOrEmpty()) SearchStatus.NOT_FOUND else SearchStatus.OK
                )
                changeUI(result)
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                changeUI(SearchTrackResult(null, SearchStatus.INTERNET_ERROR))
            }
        })
    }

    private fun showProgressBar() {
        binding.searchProgressBar.visibility = View.VISIBLE
        binding.searchPlaceholderLayout.visibility = View.GONE
        binding.searchRecycleView.visibility = View.GONE
        binding.searchRefreshButton.visibility = View.GONE
    }

    private fun changeUI(result: SearchTrackResult) {
        binding.searchProgressBar.visibility = View.GONE

        when (result.status) {
            SearchStatus.OK -> {
                result.tracks!!.forEach { track -> track.setTime() }
                val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter
                adapter.tracks = result.tracks!!
                adapter.notifyDataSetChanged()
                binding.searchRecycleView.visibility = View.VISIBLE
                binding.searchPlaceholderLayout.visibility = View.GONE
            }

            SearchStatus.NOT_FOUND -> {
                binding.searchRecycleView.visibility = View.GONE
                binding.searchPlaceholderLayout.visibility = View.VISIBLE
                binding.searchRefreshButton.visibility = View.GONE
                binding.searchPlaceholderText.text = getString(R.string.search_not_found)
                binding.searchPlaceholderIcon.setImageResource(R.drawable.no_tracks_found_placeholder)
            }

            SearchStatus.INTERNET_ERROR -> {
                binding.searchRecycleView.visibility = View.GONE
                binding.searchPlaceholderLayout.visibility = View.VISIBLE
                binding.searchRefreshButton.visibility = View.VISIBLE
                binding.searchPlaceholderText.text = getString(R.string.search_internet_error)
                binding.searchPlaceholderIcon.setImageResource(R.drawable.connection_error_placeholder)
            }
        }
    }

    private fun setButtonsAction() {
        binding.btnSettingsBack.setOnClickListener({
            finish()
        })

        binding.searchRefreshButton.setOnClickListener({
            search(lastSearch)
        })
    }

    private fun setRecyclerView() {
        val adapter = SearchTrackAdapter(prefs, this)
        binding.searchRecycleView.adapter = adapter
    }

    private fun setEditTextActions(savedInstanceState: Bundle?) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        val editText = binding.searchEditText
        val clearBtn = binding.btnClearTextSearch


        if (savedInstanceState != null) {
            editText.setText(editTextValue)
        }

        clearBtn.setOnClickListener({
            editText.setText("")
            editTextValue = ""
            lastSearch = ""
            val adapter = binding.searchRecycleView.adapter as SearchTrackAdapter

            if (searchHistory.isHistoryEmpty()) {
                adapter.tracks = emptyList()
                adapter.notifyDataSetChanged()
                binding.searchRecycleView.visibility = View.GONE
            }

            binding.searchPlaceholderLayout.visibility = View.GONE
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)

        })

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
        outState.putString(editTextKey, editTextValue)
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
        editTextValue = savedInstanceState.getString(editTextKey, "")
    }

    private fun setButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val editTextKey = "editText"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}