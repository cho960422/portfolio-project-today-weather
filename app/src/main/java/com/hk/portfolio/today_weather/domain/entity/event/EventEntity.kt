package com.hk.portfolio.today_weather.domain.entity.event

import java.time.LocalDate
import java.time.LocalDateTime

data class EventEntity(
    val id:String,
    val eventName:String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val place: PlaceEntity,
    val alarm: LocalDateTime?
)
