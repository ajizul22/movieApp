package com.reserach.movieapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.reserach.movieapp.R
import com.reserach.movieapp.data.remote.MovieApi
import com.reserach.movieapp.data.remote.RetrofitClient
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.data.remote.response.ReviewsResponse
import com.reserach.movieapp.databinding.ActivityDetailMoviesBinding
import com.reserach.movieapp.util.adapter.ReviewsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class DetailMoviesActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailMoviesBinding
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var coroutineScope: CoroutineScope

    private lateinit var adapterReviews: ReviewsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var pageReviews = 1

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_detail_movies)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        val service = RetrofitClient.getApiClient()?.create(MovieApi::class.java)
        viewModel = ViewModelProvider(this).get(DetailMovieViewModel::class.java)

        if (service != null) {
            viewModel.setService(service)
        }

        adapterReviews = ReviewsAdapter()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        initRequestData()
        initComponent()
        initData()

    }



    private fun initComponent() {
        bind.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }

            rcvReviews.adapter = adapterReviews
            rcvReviews.layoutManager = layoutManager
            rcvReviews.setHasFixedSize(true)

        }
    }

    private fun initRequestData() {
        val intent = intent
        id = intent.getIntExtra("ID", 0)
        if (id != 0) {
            viewModel.callDetailMovieApi(id)
            viewModel.callReviewsMovie(id,pageReviews)
        }
    }

    private fun initData() {
        viewModel.dataMovies.observe(this, Observer {
            if (it != null) {
                bind.tvTitle.text = it.original_title
                bind.tvOverview.text = it.overview

                val genre = it.genres.joinToString { it -> it.name }
                bind.tvGenre.text = "Genre : $genre"

                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500${it.poster_path}")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(bind.ivMoviePoster)
            }
        })

        viewModel.listReview.observe(this, Observer {
            if (it != null) {
                val list = ArrayList<ReviewsResponse.Results>()
                list.addAll(it)
                adapterReviews.setList(list)
            }
        })

        viewModel.isSuccessReviews.observe(this, Observer {
            if (it) {
                bind.tvReviewsNotFound.visibility = View.GONE
            } else {
                bind.tvReviewsNotFound.visibility = View.VISIBLE
            }
        })

    }



}