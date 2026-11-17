package com.example.moviebox.feature.home.state

import com.example.moviebox.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)
