package com.hk.portfolio.today_weather.domain.entity.event

import com.hk.portfolio.today_weather.domain.entity.weather.WeatherConditionEntity
import java.time.LocalDate
import java.time.LocalDateTime

data class EventEntity(
    val id:String,
    val eventName:String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val place: PlaceEntity,
    val alarm: LocalDateTime?,
    val updateAt: LocalDateTime,
    val broadcastId: Int?
)
