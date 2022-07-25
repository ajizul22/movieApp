package com.reserach.movieapp.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MovieDetailsResponse(
    val id: Int,
    val genres: List<GenresResponse.Genres>,
    val original_title: String,
    val poster_path: String,
    val overview: String
    )
