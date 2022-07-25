package com.reserach.movieapp.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val overview: String?,
    val poster_path: String,
    val backdrop_path: String,
    val original_title: String
):Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}
