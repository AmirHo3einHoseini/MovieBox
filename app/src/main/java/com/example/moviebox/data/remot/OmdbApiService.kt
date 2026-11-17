package com.example.moviebox.data.remot


import android.R.attr.rating
import android.util.Log
import com.example.moviebox.domain.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.cancellation.CancellationException


class OmdbApiService {
    private val movieCache = ConcurrentHashMap<String, Movie>()


    private companion object {
        const val API_KEY = "d64ea631"
        const val MAX_CONCURRENT_REQUESTS = 5
        const val TIMEOUT = 10_000
    }

    suspend fun getMovies(titles: List<String>): List<Movie> =
        coroutineScope {
            val semaphore = Semaphore(MAX_CONCURRENT_REQUESTS)
            titles.map { title ->
                async(Dispatchers.IO) {
                    semaphore.withPermit {
                        fetchMovie(title)
                    }
                }
            }.awaitAll().filterNotNull()
        }


    private fun fetchMovie(title: String): Movie? {
        val cacheKey = title.trim().lowercase()

        movieCache[cacheKey]?.let {
            return it
        }
        var connection: HttpURLConnection? = null

        return try {
            val encodedTitle = URLEncoder.encode(title, "UTF-8")

            val url = URL(
                "https://www.omdbapi.com/?t=$encodedTitle&apikey=$API_KEY"
            )

            connection = url.openConnection() as HttpURLConnection

            connection.apply {
                requestMethod = "GET"
                connectTimeout = TIMEOUT
                readTimeout = TIMEOUT
            }

            val responseCode = connection.responseCode

            if (responseCode !in 200..299) {
                Log.e("OMDb", "HTTP error $responseCode for $title")
                return null
            }

            val response = connection.inputStream
                .bufferedReader()
                .use { it.readText() }

            val json = JSONObject(response)

            if (json.optString("Response") != "True") {
                Log.e(
                    "OMDb",
                    "API failed for $title: ${json.optString("Error")}"
                )
                return null
            }

            val poster = json.optString("Poster")

            if (poster.isBlank() || poster == "N/A") {
                return null
            }

            val movie=  Movie(
                imdbId = json.optString("imdbID"),
                title = json.optString("Title"),
                year = json.optString("Year"),
                rating = json.optString("imdbRating"),
                posterUrl = poster,
                totalSeasons = json.optString("totalSeasons")
                    .takeIf { it.isNotBlank() && it != "N/A" }
            )

            movieCache[cacheKey] = movie
            
            movie
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("OMDb", "Failed to load $title", e)
            null
        } finally {
            connection?.disconnect()
        }
    }

}


