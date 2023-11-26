package com.hk.portfolio.today_weather.domain.repository

import com.hk.portfolio.today_weather.core.SearchCategoryEnum
import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun insertHistory(query:String, category:Int)
    suspend fun getHistoryList(category: SearchCategoryEnum, query:String): List<SearchHistoryData>
}