package com.example.moviebox.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviebox.feature.home.presentation.components.MovieCarousel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadMovies()
    }
    val uiState  by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when{
            uiState.isLoading->{
                CircularProgressIndicator(
                    color = Color.Red
                )
            }
            uiState.error !=null->{
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.error!!,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {viewModel.loadMovies()}
                    ) {
                        Text(text = "تلاش مجدد")
                    }
                }
            }
            uiState.movies.isNotEmpty()->{
                MovieCarousel(movies = uiState .movies)
            }
        }

    }

}