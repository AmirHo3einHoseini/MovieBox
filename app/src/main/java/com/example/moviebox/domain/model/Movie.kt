package com.example.moviebox.domain.model

import android.R
import android.media.Rating


data class Movie(
    val imdbId:String? = null,
    val title:String = "The Last of Us",
    val year:  String = "2025",
    val rating: String = "8.9",
    val posterUrl: String ,
    val totalSeasons: String? = null,
    val network: String? = null
)
