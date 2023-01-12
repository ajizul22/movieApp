package com.reserach.movieapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reserach.movieapp.data.remote.MovieApi
import com.reserach.movieapp.data.remote.response.MovieDetailsResponse
import com.reserach.movieapp.data.remote.response.MovieVideoResponse
import com.reserach.movieapp.data.remote.response.ReviewsResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailMovieViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: MovieApi

    val isSuccess = MutableLiveData<Boolean>()
    val dataMovies = MutableLiveData<MovieDetailsResponse>()
    val isSuccessReviews = MutableLiveData<Boolean>()
    val listReview = MutableLiveData<List<ReviewsResponse.Results>>()
    val isSuccessVideos = MutableLiveData<Boolean>()
    val moviesVideo = MutableLiveData<List<MovieVideoResponse.Results>>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: MovieApi) {
        this.service = service
    }



    fun callDetailMovieApi(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getMovieDetail(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccess.value = false
                    }
                }
            }

            if (result is MovieDetailsResponse) {
                isSuccess.value = true
                dataMovies.value = result
            }
        }
    }

    fun callReviewsMovie(id: Int, page: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getMovieReview(id, page)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccessReviews.value = false
                    }
                }
            }

            if (result is ReviewsResponse) {
                if (result.results.isNotEmpty()) {
                    isSuccessReviews.value = true
                    listReview.value = result.results
                } else {
                    isSuccessReviews.value = false
                }

            }
        }
    }

    fun callMovieVideo(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getMovieVideo(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccessVideos.value = false
                    }
                }
            }

            if (result is MovieVideoResponse) {
                if (result.results.isNotEmpty()) {
                    isSuccessVideos.value = true
                    moviesVideo.value = result.results
                } else {
                    isSuccessVideos.value = false
                }
            }
        }
    }

}