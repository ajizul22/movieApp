package com.reserach.movieapp.util.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.reserach.movieapp.R
import com.reserach.movieapp.data.remote.response.Movie
import com.reserach.movieapp.data.remote.response.ReviewsResponse
import com.reserach.movieapp.databinding.ItemMovieBinding
import com.reserach.movieapp.databinding.ItemReviewsBinding

class ReviewsAdapter: RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {
    private val list = ArrayList<ReviewsResponse.Results>()

    fun setList(data: ArrayList<ReviewsResponse.Results>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ReviewsViewHolder(val bind: ItemReviewsBinding): RecyclerView.ViewHolder(bind.root) {
        fun bind(data: ReviewsResponse.Results) {
            bind.apply {
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w500${data.author_details.avatar_path}")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(profileImage)

                tvUsername.text = data.author_details.username
                tvOverview.text = data.content

                if (data.content.length >= 60) {
                    tvReadMore.visibility = View.VISIBLE
                } else {
                    tvReadMore.visibility = View.GONE
                }

                // expand content reviews
                var isExpand = false
                itemView.setOnClickListener {
                    if (!isExpand) {
                        isExpand = true
                        tvOverview.maxLines = 2
                        tvOverview.ellipsize = TextUtils.TruncateAt.END
                        tvReadMore.visibility = View.VISIBLE
                    } else {
                        isExpand = false
                        tvOverview.maxLines = Int.MAX_VALUE
                        tvReadMore.visibility = View.GONE
                        tvOverview.ellipsize = null
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val view = ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}