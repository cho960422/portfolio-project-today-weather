package com.hk.portfolio.today_weather.domain.usecase.event

import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.usecase.BaseUseCase
import java.time.LocalDate
import javax.inject.Inject

class GetEventListUseCase @Inject constructor(
    private val repository: EventRepository
): BaseUseCase<LocalDate, List<EventEntity>> {
    override suspend fun invoke(param: LocalDate): List<EventEntity> {
        return repository.getEventList(param)
    }
}