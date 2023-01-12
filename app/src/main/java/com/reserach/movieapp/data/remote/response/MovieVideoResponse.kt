package com.reserach.movieapp.data.remote.response

data class MovieVideoResponse(val results: List<Results>) {
    data class Results(
        val id: String,
        val site: String,
        val official: Boolean,
        val key: String
    )
}
