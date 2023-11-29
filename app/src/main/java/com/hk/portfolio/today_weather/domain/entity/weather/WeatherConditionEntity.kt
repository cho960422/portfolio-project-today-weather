package com.hk.portfolio.today_weather.domain.entity.weather

import com.hk.portfolio.today_weather.core.WeatherConditionEnum
import java.time.LocalDate
import java.time.LocalDateTime

data class WeatherConditionEntity(
    val id: String,
    val nx: Double,
    val ny: Double,
    val date: LocalDate,
    val baseDateTime: LocalDateTime,
    val weatherCondition: WeatherConditionEnum,
    val description: String,
    val eventLocationId: String
)
