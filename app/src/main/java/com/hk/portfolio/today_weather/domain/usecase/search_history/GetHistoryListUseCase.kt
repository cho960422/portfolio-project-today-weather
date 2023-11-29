package com.hk.portfolio.today_weather.domain.usecase.search_history

import com.hk.portfolio.today_weather.data.dto.room.SearchHistoryData
import com.hk.portfolio.today_weather.domain.entity.GetHistoryRequest
import com.hk.portfolio.today_weather.domain.repository.SearchHistoryRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
): CoroutineUseCase<GetHistoryRequest, List<SearchHistoryData>> {
    override suspend fun invoke(param: GetHistoryRequest): List<SearchHistoryData> {
        return searchHistoryRepository.getHistoryList(param.category, param.query)
    }
}