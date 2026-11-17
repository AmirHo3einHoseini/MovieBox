package com.example.moviebox.data.remot


import com.example.moviebox.domain.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class OmdbApiService {

    private companion object {
        const val API_KEY = "d64ea631"
    }

    suspend fun getMovies(titles: List<String>): List<Movie> =
        withContext(Dispatchers.IO) {
            titles.mapNotNull { title ->
                try {
                    val encodedTitle = URLEncoder.encode(title, "UTF-8")
                    val url = URL(
                        "https://www.omdbapi.com/" +
                                "?t=$encodedTitle" +
                                "&apikey=$API_KEY"
                    )
                    val connection = url.openConnection() as HttpURLConnection
                    try {
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 10000
                        connection.readTimeout = 10000

                        val response = connection.inputStream
                            .bufferedReader()
                            .use { it.readText() }

                        val json = JSONObject(response)
                        android.util.Log.d("OMDb", "Response: $response")

                        if (json.optString("Response") != "True") {
                            return@mapNotNull null
                        }

                        val poster = json.optString("Poster")

                        if (poster.isBlank() || poster == "N/A") {
                            return@mapNotNull null
                        }
                        Movie(
                            imdbId = json.optString("imdbID"),
                            title = json.optString("Title"),
                            year = json.optString("Year"),
                            rating = json.optString("imdbRating"),
                            posterUrl = poster,
                            totalSeasons = json.optString(
                                "totalSeasons"
                            ).takeIf {
                                it.isNotBlank() && it != "N/A"
                            }
                        )



                    }finally {
                        connection.disconnect()
                    }
                } catch (e: Exception) {
                    android.util.Log.e(
                        "OMDb",
                        "Failed to load $title",
                        e
                    )
                    null
                }

            }
        }


}