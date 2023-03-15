package com.reserach.movieapp.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithChart(
    @Embedded val orderTableModel: OrderTableModel,
    @Relation(
        parentColumn = "order_id",
        entityColumn = "chart_order_id"
    )
    val chart: List<ChartTableModel>
)
