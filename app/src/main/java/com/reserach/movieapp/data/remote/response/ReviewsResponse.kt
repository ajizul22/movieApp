package com.reserach.movieapp.data.remote.response

data class ReviewsResponse(val results: List<Results>) {
    data class Results(
        val author: String,
        val content: String,
        val author_details: AuthorDetails
    ) {
        data class AuthorDetails(val username: String, val avatar_path: String)
    }
}
