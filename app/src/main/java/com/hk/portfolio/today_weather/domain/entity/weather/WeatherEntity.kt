package com.hk.portfolio.today_weather.domain.entity.weather

import com.hk.portfolio.today_weather.core.WeatherCategoryEnum
import java.time.LocalDateTime

data class WeatherEntity(
    val category: WeatherCategoryEnum,
    val dateTime: LocalDateTime,
    val baseDateTime: LocalDateTime,
    val nx: Double,
    val ny: Double,
    val valueForCategory: String
)
