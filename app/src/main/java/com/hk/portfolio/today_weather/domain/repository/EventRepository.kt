package com.hk.portfolio.today_weather.domain.repository

import androidx.paging.PagingSource
import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.domain.entity.event.EventAndWeatherEntity
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EventRepository {
    suspend fun getEventListAll(startDate: LocalDate?):List<EventAndWeatherEntity>
    fun getEventPagingSource(): PagingSource<Int, EventEntity>
    suspend fun insert(eventLocation: EventLocation): Boolean
    fun getTodayEvent(): Flow<List<EventAndWeatherEntity>>
}