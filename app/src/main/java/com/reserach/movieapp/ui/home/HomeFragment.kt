package com.reserach.movieapp.ui.home

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.reserach.movieapp.R
import com.reserach.movieapp.data.remote.MovieApi
import com.reserach.movieapp.data.remote.RetrofitClient
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.databinding.FragmentHomeBinding
import com.reserach.movieapp.ui.detail.DetailMoviesActivity
import com.reserach.movieapp.util.GlobalFunc
import com.reserach.movieapp.util.adapter.PopularMovieAdapter
import com.reserach.movieapp.util.adapter.TopMovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel


class HomeFragment: Fragment() {
    private lateinit var bind: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var service: MovieApi

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var adapterPopular: PopularMovieAdapter
    private lateinit var layoutManagerPopular: LinearLayoutManager

    private lateinit var adapterTop: TopMovieAdapter
    private lateinit var layoutManagerTop: LinearLayoutManager

    private lateinit var globalFunc: GlobalFunc

    private var pagePopular = 1
    private var pageTop = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = RetrofitClient.getApiClient()!!.create(MovieApi::class.java)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            HomeViewModel::class.java
        )

        adapterPopular = PopularMovieAdapter()
        layoutManagerPopular = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        adapterTop = TopMovieAdapter()
        layoutManagerTop = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.setService(service)

        globalFunc = GlobalFunc()

        initRequestData()
        initComponent()
        initData()

        return bind.root
    }

    private fun initComponent() {
        bind.apply {
            rcvPopularMovie.layoutManager = layoutManagerPopular
            rcvPopularMovie.adapter = adapterPopular
            rcvPopularMovie.setHasFixedSize(true)

            rcvPopularMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManagerPopular.childCount
                    val pastVisibleItem = layoutManagerPopular.findFirstVisibleItemPosition()
                    val total = adapterPopular.itemCount

                    if (visibleItemCount + pastVisibleItem >= total) {
                        pagePopular++
                        viewModel.callPopularApi(pagePopular)
                        showLoading(2, true)
                    }

                    super.onScrolled(recyclerView, dx, dy)

                }
            })

            rcvTopMovie.layoutManager = layoutManagerTop
            rcvTopMovie.adapter = adapterTop
            rcvTopMovie.setHasFixedSize(true)

            rcvTopMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManagerTop.childCount
                    val pastVisibleItem = layoutManagerTop.findFirstVisibleItemPosition()
                    val total = adapterTop.itemCount

                    if (visibleItemCount + pastVisibleItem >= total) {
                        pageTop++
                        viewModel.callTopApi(pageTop)
                        showLoading(3, true)
                    }

                    super.onScrolled(recyclerView, dx, dy)

                }
            })

            adapterPopular.setOnItemClickCallback(object : PopularMovieAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Movie) {
                    val intent = Intent(activity, DetailMoviesActivity::class.java)
                    intent.putExtra("ID", data.id)
                    startActivity(intent)
//                    Toast.makeText(context, data.original_title, Toast.LENGTH_SHORT).show()
                }
            })

            adapterTop.setOnItemClickCallback(object : TopMovieAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Movie) {
                    val intent = Intent(activity, DetailMoviesActivity::class.java)
                    intent.putExtra("ID", data.id)
                    startActivity(intent)
//                    Toast.makeText(context, data.original_title, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun initRequestData() {
        viewModel.callUpcomingApi()
        showLoading(1, true)
        viewModel.callPopularApi(pagePopular)
        showLoading(2, true)
        viewModel.callTopApi(pageTop)
        showLoading(3, true)
    }

    private fun initData() {
        viewModel.listUpComing.observe(viewLifecycleOwner) {
            if (it != null) {
                val listImage = ArrayList<SlideModel>()
                for ((index, value) in it.withIndex()) {
                    listImage.add(
                        SlideModel(
                            "${value.baseUrl}${value.backdrop_path}",
                            value.original_title
                        )
                    )
                    if (index >= 4) {
                        break
                    }
                }

                if (listImage != null) {
                    bind.ivSliderComingsoon.setImageList(listImage, ScaleTypes.FIT)
                }
                showNoData(1, false)

//                adapterUpComing.setList(it as ArrayList<Movie>)
            } else {
                showNoData(1, true)
            }
        }

        viewModel.isSuccessComingsoon.observe(viewLifecycleOwner) {
            if (it) {
                showLoading(1, false)
            } else {
               showNoData(1, true)
                viewModel.errorMessageUpcoming.observe(viewLifecycleOwner) { code ->
                    if (code != null || code != 0) {
                        showLoading(1, false)
                        globalFunc.showDialogError(requireContext(), globalFunc.responseError(code))
                    }
                }
            }
        }

        viewModel.listPopular.observe(viewLifecycleOwner) {
            if (it != null) {
                val list = ArrayList<Movie>()
                list.addAll(it)
                adapterPopular.setList(list)
                showNoData(2, false)
            } else {
                showNoData(2, true)
            }
        }

        viewModel.isSuccessPopular.observe(viewLifecycleOwner) {
            if (it) {
                showLoading(2, false)
            } else {
                showNoData(2, true)
                showLoading(2, false)
            }
        }

        viewModel.listTop.observe(viewLifecycleOwner) {
            if (it != null) {
                val list = ArrayList<Movie>()
                list.addAll(it)
                adapterTop.setList(list)
                showNoData(3, false)
            } else {
                showNoData(3, true)
            }
        }

        viewModel.isSuccessTop.observe(viewLifecycleOwner) { it ->
            if (it) {
                showLoading(3, false)
            } else {
                showLoading(3, false)
                showNoData(3, true)
            }
        }


    }

    private fun showNoData(code: Int, state: Boolean) {
        // 1 is coming soon
        // 2 is popular movies
        // 3 is top rated movies
        when (code) {
            1 -> {
                if (state) {
                    bind.apply {
                        ivSliderComingsoon.visibility = View.GONE
                        ivNoDataUpcoming.visibility = View.VISIBLE
                    }
                } else {
                    bind.apply {
                        ivSliderComingsoon.visibility = View.VISIBLE
                        ivNoDataUpcoming.visibility = View.GONE
                    }
                }
            }

            2 -> {
                if (state) {
                    bind.apply {
                        rcvPopularMovie.visibility = View.GONE
                        ivNoDataPopular.visibility = View.VISIBLE
                    }
                } else {
                    bind.apply {
                        rcvPopularMovie.visibility = View.VISIBLE
                        ivNoDataPopular.visibility = View.GONE
                    }
                }
            }

            3 -> {
                if (state) {
                    bind.apply {
                        rcvTopMovie.visibility = View.GONE
                        ivNoDataTop.visibility = View.VISIBLE
                    }
                } else {
                    bind.apply {
                        rcvTopMovie.visibility = View.VISIBLE
                        ivNoDataTop.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showLoading(type: Int, state: Boolean) {
        // 1 is coming soon
        // 2 is popular movies
        // 3 is top rated movies
        when (type) {
            1 -> {
                if (state) {
                    bind.progressbarComingsoon.visibility = View.VISIBLE
                } else {
                    bind.progressbarComingsoon.visibility = View.GONE
                }
            }

            2 -> {
                if (state) {
                    bind.progressbarPopular.visibility = View.VISIBLE
                } else {
                    bind.progressbarPopular.visibility = View.GONE
                }
            }

            3 -> {
                if (state) {
                    bind.progressbarTopmovie.visibility = View.VISIBLE
                } else {
                    bind.progressbarTopmovie.visibility = View.GONE
                }
            }
        }

    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}