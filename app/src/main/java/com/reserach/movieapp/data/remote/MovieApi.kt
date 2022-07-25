package com.reserach.movieapp.data.remote

import com.reserach.movieapp.BuildConfig
import com.reserach.movieapp.data.remote.response.GenresResponse
import com.reserach.movieapp.data.remote.response.MovieDetailsResponse
import com.reserach.movieapp.data.remote.response.MovieResponse
import com.reserach.movieapp.data.remote.response.ReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {
    companion object {
        const val BASE_URL = "http://api.themoviedb.org/3/"
        const val API_KEY = BuildConfig.MOVIEDB_API_KEY
    }

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpComingMovies(): MovieResponse

    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/top_rated?api_key=$API_KEY")
    suspend fun getTopMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET("genre/movie/list?api_key=$API_KEY")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie?api_key=$API_KEY")
    suspend fun getMoviesByGenres(
        @Query("with_genres") genres: String,
        @Query("page") page:Int
    ): MovieResponse

    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetailsResponse


    @GET("movie/{movie_id}/reviews?api_key=$API_KEY")
    suspend fun getMovieReview(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): ReviewsResponse

}