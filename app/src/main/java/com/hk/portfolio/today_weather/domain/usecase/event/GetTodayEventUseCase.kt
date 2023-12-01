package com.hk.portfolio.today_weather.domain.usecase.event

import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.usecase.weather.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
): BaseUseCase<Unit, Flow<List<EventAndWeatherEntity>>> {
    override fun invoke(param: Unit): Flow<List<EventAndWeatherEntity>> {
        return eventRepository.getTodayEvent()
    }
}