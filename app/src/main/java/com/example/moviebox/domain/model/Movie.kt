package com.example.moviebox.domain.model


data class Movie(
    val imdbId:String,
    val title:String,
    val year:  String,
    val rating: String,
    val posterUrl: String ,
    val totalSeasons: String? = null,
    val network: String? = null
)
