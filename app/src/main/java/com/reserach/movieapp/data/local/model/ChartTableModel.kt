package com.reserach.movieapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Chart")
data class ChartTableModel (
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "movie_title") val movieTitle: String,
    @ColumnInfo(name = "total_item") val totalItem: Int,
    @ColumnInfo(name = "movie_img") val movieImage: String,
    @ColumnInfo(name = "chart_order_id") val cOrderId: Int?
    ) {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "chart_id")
        var chartId: Int? = null
}