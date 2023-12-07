package com.hk.portfolio.today_weather.domain.usecase.event

import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
): CoroutineUseCase<EventAndWeatherEntity, Unit> {
    override suspend fun invoke(param: EventAndWeatherEntity) {
        eventRepository.deleteEvent(param)
    }
}