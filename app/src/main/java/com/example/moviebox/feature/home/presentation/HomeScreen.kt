package com.example.moviebox.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.moviebox.feature.home.presentation.components.MovieCard
import com.example.moviebox.feature.home.presentation.components.MovieCarousel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,

    ) {
    Box(
        modifier= modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){
        MovieCarousel()
    }

}