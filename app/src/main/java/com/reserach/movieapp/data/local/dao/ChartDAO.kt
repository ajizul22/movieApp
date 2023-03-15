package com.reserach.movieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.reserach.movieapp.data.local.model.ChartTableModel

@Dao
interface ChartDAO {

    @Insert
    fun insertChart(chartTableModel: ChartTableModel)

    @Delete
    fun deleteChart(chartTableModel: ChartTableModel)

    @Update
    fun updateChart(chartTableModel: ChartTableModel)
}