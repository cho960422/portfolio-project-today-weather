package com.hk.portfolio.today_weather.domain.usecase.search_history

import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import com.hk.portfolio.today_weather.domain.repository.SearchHistoryRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class DeleteHistoryUseCase @Inject constructor(
    private val historyRepository: SearchHistoryRepository
): CoroutineUseCase<SearchHistoryData, Unit> {
    override suspend fun invoke(param: SearchHistoryData) {
        historyRepository.deleteHistory(param)
    }
}