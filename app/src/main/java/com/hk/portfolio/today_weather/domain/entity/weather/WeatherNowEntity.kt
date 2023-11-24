package com.hk.portfolio.today_weather.domain.entity.weather

import com.hk.portfolio.today_weather.core.WeatherNowCategoryEnum
import java.time.LocalDateTime

data class WeatherNowEntity(
    val baseDateTime: LocalDateTime,
    val category: WeatherNowCategoryEnum,
    val nx: Double,
    val ny: Double,
    val valueForCategory: Int
)
