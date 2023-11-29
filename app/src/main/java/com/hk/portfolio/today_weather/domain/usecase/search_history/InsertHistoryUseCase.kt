package com.hk.portfolio.today_weather.domain.usecase.search_history

import com.hk.portfolio.today_weather.domain.entity.InsertHistoryRequest
import com.hk.portfolio.today_weather.domain.repository.SearchHistoryRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class InsertHistoryUseCase @Inject constructor(
    private val historyRepository: SearchHistoryRepository
): CoroutineUseCase<InsertHistoryRequest, Unit> {
    override suspend fun invoke(param: InsertHistoryRequest) {
        historyRepository.insertHistory(param.query, param.category.category)
    }
}