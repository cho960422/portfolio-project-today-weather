package com.hk.portfolio.today_weather.domain.entity.weather

import com.hk.portfolio.today_weather.core.WeatherShortCategoryEnum
import java.time.LocalDateTime

data class WeatherShortEntity(
    val baseDateTime: LocalDateTime,
    val category: WeatherShortCategoryEnum,
    val nx: Double,
    val ny: Double,
    val valueForCategory: Float
)
