package com.example.playlistmaker.ui.search.viewmodel.model

import android.content.Context
import com.example.playlistmaker.R

class SearchConstants (
    app: Context
){
    val internetError = app.getString(R.string.search_internet_error)
    val notFound = app.getString(R.string.search_not_found)
}