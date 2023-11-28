package com.hk.portfolio.today_weather.domain.repository

import com.hk.portfolio.today_weather.data.dto.room.EventLocation
import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import java.time.LocalDate

interface EventRepository {
    suspend fun getEventList(startDate: LocalDate?): List<EventEntity>
    suspend fun insert(eventLocation: EventLocation): Boolean
}