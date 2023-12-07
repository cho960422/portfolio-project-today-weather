package com.hk.portfolio.today_weather.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.hk.portfolio.today_weather.core.AppDatabase
import com.hk.portfolio.today_weather.core.SearchCategoryEnum
import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import com.hk.portfolio.today_weather.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val db: AppDatabase
): SearchHistoryRepository {
    val dao = db.searchHistoryDao()
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun insertHistory(query:String, category:Int) {
        dao.upsertHistory(
            SearchHistoryData(
                query, category, LocalDateTime.now()
            )
        )
    }

    override suspend fun getHistoryList(
        category: SearchCategoryEnum,
        query: String
    ): List<SearchHistoryData> {
        return dao.getHistoryListFlow(category = category.category, query = "%${query}%")
    }

    override suspend fun deleteHistory(data: SearchHistoryData) {
        dao.deleteHistory(data)
    }
}