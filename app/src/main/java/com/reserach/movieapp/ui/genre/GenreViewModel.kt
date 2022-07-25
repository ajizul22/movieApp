package com.reserach.movieapp.ui.genre

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reserach.movieapp.data.remote.MovieApi
import com.reserach.movieapp.data.remote.response.GenresResponse
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.data.remote.response.MovieResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class GenreViewModel: ViewModel(), CoroutineScope {

    private lateinit var service: MovieApi
    val isSuccessGenres = MutableLiveData<Boolean>()
    val isSuccessMovies = MutableLiveData<Boolean>()
    val listGenres = MutableLiveData<List<GenresResponse.Genres>>()
    val listMovies = MutableLiveData<List<Movie>>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: MovieApi) {
        this.service = service
    }

    fun callGenresApi() {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getGenres()
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccessGenres.value = false
                    }
                }
            }

            if (result is GenresResponse) {
                isSuccessGenres.value = true
                listGenres.value = result.genres
            }
        }
    }

    fun callMoviesByGenresApi(page: Int, genres: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getMoviesByGenres(genres, page)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccessMovies.value = false
                    }
                }
            }

            if (result is MovieResponse) {
                isSuccessMovies.value = true
                listMovies.value = result.results
            }
        }
    }
}