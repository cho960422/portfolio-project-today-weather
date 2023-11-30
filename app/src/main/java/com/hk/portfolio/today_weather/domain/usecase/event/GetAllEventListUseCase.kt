package com.hk.portfolio.today_weather.domain.usecase.event

import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import java.time.LocalDate
import javax.inject.Inject

class GetAllEventListUseCase @Inject constructor(
    private val repository: EventRepository
): CoroutineUseCase<LocalDate, List<EventAndWeatherEntity>> {
    override suspend fun invoke(param: LocalDate): List<EventAndWeatherEntity> {
        return repository.getEventListAll(param)
    }
}