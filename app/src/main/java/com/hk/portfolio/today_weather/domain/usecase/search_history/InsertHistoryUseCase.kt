package com.hk.portfolio.today_weather.domain.usecase.search_history

import com.hk.portfolio.today_weather.domain.entity.InsertHistoryRequest
import com.hk.portfolio.today_weather.domain.repository.SearchHistoryRepository
import com.hk.portfolio.today_weather.domain.usecase.BaseUseCase
import javax.inject.Inject

class InsertHistoryUseCase @Inject constructor(
    private val historyRepository: SearchHistoryRepository
): BaseUseCase<InsertHistoryRequest, Unit> {
    override suspend fun invoke(param: InsertHistoryRequest) {
        historyRepository.insertHistory(param.query, param.category.category)
    }
}