package com.reserach.movieapp.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.databinding.ItemMovieBinding

class PopularMovieAdapter : RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    private val list = ArrayList<Movie>()

    fun setList(movies: ArrayList<Movie>) {
        list.addAll(movies)
        notifyDataSetChanged()
    }

    inner class PopularMovieViewHolder(val bind: ItemMovieBinding) : RecyclerView.ViewHolder(bind.root)  {
        fun bind(data: Movie) {
            bind.apply {
                Glide.with(itemView)
                    .load("${data.baseUrl}${data.poster_path}")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivMoviePoster)

                tvMovieTitle.text = data?.original_title

                bind.root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(data)
                }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        val view = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback{
        fun onItemClicked(data: Movie)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}