package com.example.playlistmaker.presentation.model

sealed interface State {

    data object Loading : State

    data class Content<T>(
        val data: List<T>
    ) : State

    data class Error(
        val message: String
    ) : State

    data class Empty(
        val message: String
    ) : State
}