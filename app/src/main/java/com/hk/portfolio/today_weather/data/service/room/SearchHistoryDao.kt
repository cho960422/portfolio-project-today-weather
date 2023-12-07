package com.hk.portfolio.today_weather.data.service.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Upsert
    fun upsertHistory(search: SearchHistoryData)

    @Query("SELECT * FROM search_history WHERE category = :category AND `query` LIKE :query ORDER BY updateAt DESC LIMIT 0, 10")
    fun getHistoryListFlow(category: Int, query: String): List<SearchHistoryData>

    @Delete
    fun deleteHistory(data: SearchHistoryData)
}