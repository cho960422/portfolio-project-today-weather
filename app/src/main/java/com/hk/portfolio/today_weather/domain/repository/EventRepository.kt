package com.hk.portfolio.today_weather.domain.repository

import com.hk.portfolio.today_weather.domain.entity.event.EventEntity
import java.time.LocalDate

interface EventRepository {
    suspend fun getEventList(startDate: LocalDate?): List<EventEntity>
}