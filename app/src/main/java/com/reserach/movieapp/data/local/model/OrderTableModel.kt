package com.reserach.movieapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "Order")
data class OrderTableModel(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "order_price") val price: BigDecimal,
    @ColumnInfo(name = "order_process") val isOrderProcess: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    var orderId: Int? = null
}
