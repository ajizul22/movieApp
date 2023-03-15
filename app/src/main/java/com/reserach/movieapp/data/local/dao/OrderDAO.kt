package com.reserach.movieapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.reserach.movieapp.data.local.model.OrderTableModel
import com.reserach.movieapp.data.local.model.OrderWithChart
import com.reserach.movieapp.data.local.model.UserTableModel

@Dao
interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(orderTableModel: OrderTableModel)

    @Update
    fun updateOrder(orderTableModel: OrderTableModel)

    @Transaction
    @Query("SELECT * FROM `Order` WHERE order_id =:orderId")
    fun getAllOrderWithChart(orderId: Int?): LiveData<OrderWithChart>

}