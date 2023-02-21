package com.reserach.movieapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.data.remote.MovieApi
import com.reserach.movieapp.data.remote.response.MovieResponse
import com.reserach.movieapp.util.GlobalFunc
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class HomeViewModel : ViewModel(), CoroutineScope {

    private lateinit var service: MovieApi
    val isSuccessComingsoon = MutableLiveData<Boolean>()
    val listUpComing = MutableLiveData<List<Movie>>()
    val isSuccessPopular = MutableLiveData<Boolean>()
    val listPopular = MutableLiveData<List<Movie>>()
    val isSuccessTop = MutableLiveData<Boolean>()
    val listTop = MutableLiveData<List<Movie>>()
    val errorMessageUpcoming = MutableLiveData<Int>()
    val globalFunc = GlobalFunc()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: MovieApi) {
        this.service = service
    }

    fun callUpcomingApi() {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getUpComingMovies()
                } catch (e: HttpException) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccessComingsoon.value = false
                        errorMessageUpcoming.value = e.code()
                    }
                }
            }

            if (result is MovieResponse) {
                isSuccessComingsoon.value = true
                listUpComing.value = result.results
            }
        }
    }

    fun callPopularApi(page: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getPopularMovies(page)
                } catch (e: HttpException) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccessPopular.value = false
                    }
                }
            }

            if (result is MovieResponse) {
                isSuccessPopular.value = true
                listPopular.value = result.results
            }
        }
    }

    fun callTopApi(page: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getTopMovies(page)
                } catch (e: HttpException) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isSuccessTop.value = false
                    }
                }
            }

            if (result is MovieResponse) {
                isSuccessTop.value = true
                listTop.value = result.results
            }
        }
    }

}