package com.reserach.movieapp.ui.genre

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reserach.movieapp.R
import com.reserach.movieapp.data.remote.MovieApi
import com.reserach.movieapp.data.remote.RetrofitClient
import com.reserach.movieapp.data.remote.response.GenresResponse
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.databinding.FragmentGenreBinding
import com.reserach.movieapp.ui.detail.DetailMoviesActivity
import com.reserach.movieapp.util.GlobalFunc
import com.reserach.movieapp.util.adapter.GenresAdapter
import com.reserach.movieapp.util.adapter.MovieByGenreAdapter
import com.reserach.movieapp.util.adapter.PopularMovieAdapter
import com.reserach.movieapp.util.adapter.TopMovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class GenreFragment: Fragment() {

    private lateinit var bind: FragmentGenreBinding
    private lateinit var viewModel: GenreViewModel
    private lateinit var service: MovieApi

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var adapterGenre: GenresAdapter
    private lateinit var layoutManagerGenre: LinearLayoutManager

    private lateinit var adapterMovies: MovieByGenreAdapter
    private lateinit var layoutManagerMovies: GridLayoutManager

    private lateinit var globalFunc: GlobalFunc

    var genres: String = ""

    private var pageMovies = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_genre, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = RetrofitClient.getApiClient()!!.create(MovieApi::class.java)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GenreViewModel::class.java)

        adapterGenre = GenresAdapter()
        layoutManagerGenre = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapterMovies = MovieByGenreAdapter()
        layoutManagerMovies = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false )

        viewModel.setService(service)

        globalFunc = GlobalFunc()

        initRequestData()
        initComponent()
        initData()

        return bind.root
    }

    private fun initRequestData() {
        viewModel.callGenresApi()
        showLoading(true, 0)
    }

    private fun initComponent() {
        bind.apply {
            rcvGenres.adapter = adapterGenre
            rcvGenres.layoutManager = layoutManagerGenre
            rcvGenres.setHasFixedSize(true)

            ivBack.setOnClickListener {
                showLoading(false, 1)
                adapterMovies.clearList()
                pageMovies = 1
            }

            adapterGenre.setOnItemClickCallback(object : GenresAdapter.OnItemClickCallback {
                override fun onItemClicked(data: GenresResponse.Genres) {
                    tvGenre.text = data.name
                    viewModel.callMoviesByGenresApi(pageMovies, data.id.toString())
                    genres = data.name
                    adapterMovies.clearList()
                    showLoading(true,0)
                }
            })

            adapterMovies.setOnItemClickCallback(object : MovieByGenreAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Movie) {
                    val intent = Intent(activity, DetailMoviesActivity::class.java)
                    intent.putExtra("ID", data.id)
                    startActivity(intent)
                }
            })



            rcvMovie.layoutManager = layoutManagerMovies
            rcvMovie.adapter = adapterMovies
            rcvMovie.setHasFixedSize(true)

            rcvMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManagerMovies.childCount
                    val pastVisibleItem = layoutManagerMovies.findFirstVisibleItemPosition()
                    val total  = adapterMovies.itemCount

                    if (visibleItemCount + pastVisibleItem>= total){
                        pageMovies++
                        viewModel.callMoviesByGenresApi(pageMovies, genres)
                        bind.progressbarMoviesScroll.visibility = View.VISIBLE
                    }

                    super.onScrolled(recyclerView, dx, dy)

                }
            })

        }
    }

    private fun initData() {
        viewModel.listGenres.observe(viewLifecycleOwner) {
            if (it != null) {
                adapterGenre.setList(it as ArrayList<GenresResponse.Genres>)
                showNoData(1, false)
            } else {
                showLoading(false, 1)
                showNoData(1, true)
            }
        }

        viewModel.isSuccessGenres.observe(viewLifecycleOwner) {
            if (it) {
                showLoading(false, 1)
            } else {
                viewModel.errorMessageGenres.observe(viewLifecycleOwner) { code ->
                    if (code != null || code != 0) {
                        showLoading(false, 1)
                        globalFunc.showDialogError(requireContext(), globalFunc.responseError(code))
                        showNoData(1, true)
                    }
                }
            }
        }

        viewModel.listMovies.observe(viewLifecycleOwner) {
            if (it != null) {
                val list = ArrayList<Movie>()
                list.addAll(it)
                adapterMovies.setList(list)
                bind.progressbarMoviesScroll.visibility = View.GONE
                showNoData(2, false)
            } else {
                showLoading(false, 2)
                showNoData(2, true)
            }
        }

        viewModel.isSuccessMovies.observe(viewLifecycleOwner) {
            if (it) {
                showLoading(false, 2)
            } else {
                viewModel.errorMessageMovies.observe(viewLifecycleOwner) { code ->
                    if (code != null || code != 0) {
                        showLoading(false, 2)
                        globalFunc.showDialogError(requireContext(), globalFunc.responseError(code))
                        showNoData(2, true)
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean, code: Int) {
        if (state) {
            bind.progressbarGenre.visibility = View.VISIBLE
            bind.rcvGenres.visibility = View.GONE
            bind.rcvMovie.visibility = View.GONE
            bind.lyNavigation.visibility = View.GONE
        } else {
            when (code) {
                // 1 for genre, 2 for movies by genre
                1 -> {
                    bind.rcvGenres.visibility = View.VISIBLE
                    bind.rcvMovie.visibility = View.GONE
                    bind.progressbarGenre.visibility = View.GONE
                    bind.lyNavigation.visibility = View.GONE

                }
                2 -> {
                    bind.rcvMovie.visibility = View.VISIBLE
                    bind.rcvGenres.visibility = View.GONE
                    bind.progressbarGenre.visibility = View.GONE
                    bind.lyNavigation.visibility = View.VISIBLE
                }
                else -> return
            }
        }
    }

    private fun showNoData(code: Int, state: Boolean) {
        // 1 is genre
        // 2 is movies by genre
        when (code) {
            1 -> {
                if (state) {
                    bind.apply {
                        rcvGenres.visibility = View.GONE
                        rcvMovie.visibility = View.GONE
                        ivNoData.visibility = View.VISIBLE
                        bind.lyNavigation.visibility = View.GONE
                    }
                } else {
                    bind.apply {
                        rcvGenres.visibility = View.VISIBLE
                        rcvMovie.visibility = View.GONE
                        ivNoData.visibility = View.GONE
                    }
                }
            }

            2 -> {
                if (state) {
                    bind.apply {
                        rcvGenres.visibility = View.GONE
                        rcvMovie.visibility = View.GONE
                        ivNoData.visibility = View.VISIBLE
                        bind.lyNavigation.visibility = View.VISIBLE
                    }
                } else {
                    bind.apply {
                        rcvGenres.visibility = View.GONE
                        rcvMovie.visibility = View.VISIBLE
                        ivNoData.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}