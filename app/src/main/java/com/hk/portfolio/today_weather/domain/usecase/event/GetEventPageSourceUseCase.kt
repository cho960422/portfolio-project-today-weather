package com.hk.portfolio.today_weather.domain.usecase.event

import androidx.paging.PagingSource
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import com.hk.portfolio.today_weather.domain.repository.EventRepository
import com.hk.portfolio.today_weather.domain.usecase.CoroutineUseCase
import com.hk.portfolio.today_weather.domain.usecase.weather.BaseUseCase
import javax.inject.Inject

class GetEventPageSourceUseCase @Inject constructor(
    private val eventRepository: EventRepository
): BaseUseCase<GetEventPageSourceUseCase.Request?, PagingSource<Int, EventAndWeatherEntity>> {
    class Request()
    override fun invoke(param: Request?): PagingSource<Int, EventAndWeatherEntity> {
        return eventRepository.getEventPagingSource()
    }
}