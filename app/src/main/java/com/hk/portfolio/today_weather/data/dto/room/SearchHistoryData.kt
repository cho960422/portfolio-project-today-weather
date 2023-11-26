package com.hk.portfolio.today_weather.data.dto.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity("search_history")
data class SearchHistoryData(
    @PrimaryKey
    val query: String,
    val category: Int,
    val updateAt: LocalDateTime
)
