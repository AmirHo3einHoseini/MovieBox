package com.example.moviebox.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebox.data.local.MovieCatalog
import com.example.moviebox.data.remote.OmdbApiService
import com.example.moviebox.feature.home.state.HomeUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val omdbApiService= OmdbApiService()



    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadMovies(){
        if (_uiState.value.movies.isNotEmpty()) return

        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }
            try {
                val movies = omdbApiService.getMovies(
                    MovieCatalog.titles
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        movies=movies,
                        error = if (movies.isEmpty()){
                            "دریافت اطلاعات با مشکل مواجه شد"
                        }else null
                    )
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "خطا در دریافت اطلاعات"
                    )
                }
            }
        }
        }
    }
