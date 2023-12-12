package com.hk.portfolio.today_weather.domain.usecase.event

import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class GetEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
): CoroutineUseCase<String, EventEntity> {
    override suspend fun invoke(param: String): EventEntity {
        return eventRepository.getEvent(id = param)
    }
}