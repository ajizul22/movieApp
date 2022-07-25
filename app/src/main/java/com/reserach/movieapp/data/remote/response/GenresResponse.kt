package com.reserach.movieapp.data.remote.response

data class GenresResponse(val genres: List<Genres>) {
    data class Genres(
        val id: Int,
        val name: String
    )
}
