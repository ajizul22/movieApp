package com.reserach.movieapp.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reserach.movieapp.data.remote.response.GenresResponse
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.databinding.ItemGenreBinding
import com.reserach.movieapp.databinding.ItemMovieBinding

class GenresAdapter: RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private val list = ArrayList<GenresResponse.Genres>()

    fun setList(movies: ArrayList<GenresResponse.Genres>) {
        list.addAll(movies)
        notifyDataSetChanged()
    }

    inner class GenresViewHolder(val bind:ItemGenreBinding): RecyclerView.ViewHolder(bind.root) {
        fun bind(data: GenresResponse.Genres) {
            bind.apply {
                tvGenre.text = data.name

                bind.root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val view = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback{
        fun onItemClicked(data: GenresResponse.Genres)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}